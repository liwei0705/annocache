package com.gitee.cs_liwei.annocache.aop;

import com.fasterxml.jackson.databind.JavaType;
import com.gitee.cs_liwei.annocache.annotation.AnnoCache;
import com.gitee.cs_liwei.annocache.client.iface.ICacheClient;
import com.gitee.cs_liwei.annocache.utils.JacksonUtil;
import com.gitee.cs_liwei.annocache.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.Duration;

@Aspect
@Component
@Slf4j
public class AnnoCacheAop {
	@Autowired
	AnnoCacheAopConfig config;

	@Autowired
	private ICacheClient redisClient;

	@Around("execution(* *(..)) && @annotation(cache)")
	public Object cached(final ProceedingJoinPoint pjp, AnnoCache cache) throws Throwable {
		log.debug("cache_config :" + config.toString());

		String strValue = null;
		Object value = null;
		String key = createCacheKey(pjp, cache);//cache key
		String lock_key = "lock_" + key;//lock key
		String backup_key = "back_" + key;//backup cache(level2 cache) key（last old version cache）

		Class<?> cls = getReturnClass(pjp);
		Type type = getReturnType(pjp);
		JavaType javaType = JacksonUtil.getCollectionType(type);

		if (cls != null && config.isCache_enable()) {
			String lockValue = redisClient.get(lock_key);
			log.debug("lockValue= " + lockValue + " backup_key = " + backup_key);
			if("true".equals(lockValue) && config.isCache_backup()){
				//a thread processing, try to get the last version cache
				strValue = redisClient.get(backup_key);
				log.debug("got last version cache, backup_key = " + backup_key);
			}else{
				strValue = redisClient.get(key);
				log.debug("got from cache, key = " + key);
			}

			if (strValue != null) {
				value = JacksonUtil.toObj(strValue, javaType);
				if (value != null) {
					log.debug("got object from cache, key = " + key);
					return value;//return cached result
				}else{
					log.debug("got value from cache but not a valid object, key = " + key);
				}
			}else{
				log.debug("got null value from cache. key = " + key);
			}
		}

		//processing lock.
		redisClient.set(lock_key, "true",  Duration.ofSeconds(10));
		value = pjp.proceed(); // processing

		//unlock after process
		redisClient.set(lock_key, "false", Duration.ofSeconds(1));

		if (value == null) {
			log.debug("processing result is null, key = " + key);
			if(!config.isCache_null()){
				return null;
			}
		}

		if(config.isCache_enable()){
			strValue = JacksonUtil.toStr(value);
			if (cache.seconds() <= 0) { // long term cache! be careful.
				redisClient.set(key, strValue, Duration.ofSeconds(-1));
				log.debug("long term cache set succeed, key = " + key);
			} else { // cache duration
				redisClient.set(key, strValue, Duration.ofSeconds(cache.seconds()));
				log.debug("cache succeed, key = " + key);

				redisClient.set(backup_key, strValue, Duration.ofSeconds(config.getCache_backup_seconds()));//level2 cache duration seconds
				log.debug("backup cache succeed, seconds=" + config.getCache_backup_seconds() + "，backup_key = " + backup_key);
			}
		}
		return value;
	}

	/**
	 * return the class of process result.
	 */
	private Class<?> getReturnClass(ProceedingJoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		Class<?> cl = method.getReturnType();
		if (Void.TYPE == cl) {
			return null;
		}
		return cl;
	}

	/**
	 * return the type of process result
	 */
	private Type getReturnType(ProceedingJoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method m = signature.getMethod();

		Type type = m.getGenericReturnType();
		return type;
	}

	/**
	 * generate the cache key of process method.
	 */
	private String createCacheKey(ProceedingJoinPoint pjp, AnnoCache cache) {
		StringBuilder key = new StringBuilder();
		StringBuilder buf = new StringBuilder();
		// class full path
		String className = pjp.getSignature().getDeclaringTypeName();
		// method name
		String methodName = pjp.getSignature().getName();
		buf.append(className).append(".").append(methodName);
		if (cache.key().length() > 0) {
			buf.append(".").append(cache.key());
		}

		for (Object arg : pjp.getArgs()) {
			if(null == arg){
				buf.append(".");
			}else{
				buf.append(".").append(safeToString(arg));
			}
		}

		// add prefix if exist.
		if (StringUtils.hasLength(cache.prefix())) {
			key.append(cache.prefix());
		}

		key.append(className.substring(className.lastIndexOf(".") + 1).trim());

		if (StringUtils.hasLength(buf)) {
			key.append(MD5Util.MD5(buf.toString()));
		}
		log.debug("cache key param = " + buf.toString() + " key=" + key);
		return key.toString();
	}

	/**
	 * return String value of a object, return "" when null.
	 */
	private String safeToString(Object obj){
		if(null == obj){
			return "";
		}else{
			return JacksonUtil.toStr(obj);
		}
	}
}