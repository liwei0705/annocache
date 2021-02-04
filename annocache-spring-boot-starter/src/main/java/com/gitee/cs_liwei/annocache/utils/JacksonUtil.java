package com.gitee.cs_liwei.annocache.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JacksonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jdk8Module())
              .registerModule(new JavaTimeModule())  ;
    }
    public static String toStr(Object value){
        try{
            return mapper.writeValueAsString(value);
        }catch (Exception ex){
            log.error("JacksonUtil_Exception",ex);
            throw new RuntimeException(ex);
        }
    }

    public static Object toObj(String json, JavaType javaType) {
        try {
            return  mapper.readValue(json, javaType);
        } catch (Exception ex) {
            log.error("JacksonUtil_Exception",ex);
            throw new RuntimeException(ex);
        }
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType type = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        return type;
    }

    public static JavaType getCollectionType(Type type) throws ClassNotFoundException {
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            System.out.println(pt.getRawType().getTypeName());

            List<Class> eles = new ArrayList<>();
            for(Type t : pt.getActualTypeArguments()){
                System.out.println(pt.getTypeName());
                eles.add(Class.forName(t.getTypeName()));
            }

            JavaType javaType = mapper.getTypeFactory().constructParametricType(Class.forName(pt.getRawType().getTypeName()), eles.toArray(new Class[]{}));
            return javaType;
        }else{
            return mapper.getTypeFactory().constructType(type);
        }
    }

}
