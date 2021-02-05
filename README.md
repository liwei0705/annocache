# annocache

#### 介绍
annocache-spring-boot-starter是一个超轻量级的注解式缓存组件。
只需要一行代码，即可实现方法返回值的自动化缓存。

#### 特色
1.和Spring提供的注解式缓存组件相比，使用方式和配置参数更加简化。

2.具备二级缓存功能，避免缓存击穿。在并发访问时只允许一个线程进行缓存内容的更新，其余访问线程从二级缓存获取最后一个版本的缓存值。

#### 使用说明
参考示例项目annocache-sample-api，这是一个简单的基于SpringBoot创建的web项目。

1.项目的pom.xml中增加依赖。
    
 ```xml
		<dependency>
			<groupId>com.gitee.cs_liwei</groupId>
			<artifactId>annocache-spring-boot-starter</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
 ```
    
2.目前支持Redis缓存方式，需在application.yml中指定Redis服务器的地址和端口。

```txt
spring:
  redis:
    cluster:
      - localhost:6379
      - localhost:6379
```
示例中采用的是本地启动的Redis服务器 localhost:6379。

如果开发机采用的是Windows操作系统，可以从 [这里](https://github.com/microsoftarchive/redis/releases/tag/win-3.2.100) 下载Redis Server的Windows版本，做本地启动测试。

3.在需要缓存的业务方法上添加 @AnnoCache(seconds = 45) 注解代码即可实现功能。seconds为缓存时长，单位为秒。
```java
/**
 * dummy business
 */
@Component
public class TestBusiness {

    @AnnoCache(seconds = 45)
    public TestModel test(int param1, Integer param2, String param3){
        TestModel model = new TestModel();
        //...
        //dummy process time cost（1000 milliseconds）
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return model;
    }
}
```

4.调用业务方法的地方，将会启用缓存。
```java
@RestController
public class TestController {
    @Autowired
    TestBusiness testBusiness;

    @RequestMapping("/test1")
    public TestModel test(@RequestParam int param1,
                          @RequestParam Integer param2,
                          @RequestParam String param3){

        //business process method with cache annotation.
        //the dummy process will cost over 1 second if no cache.
        return testBusiness.test(param1, param2, param3);
    }
}
```

5.启动annocache-sample-api项目，在本地浏览器中访问：
```
http://localhost:8080/test1?param1=1&param2=2&param3=3
```
可以观察到，没有建立缓存的时候API返回耗时约1000毫秒以上，建立缓存后则缩短为20毫秒以内。

#### 注意点
1.添加缓存注解的方法参数不要为Object型，所有参数都会参与缓存Key的构建。实际上，坚持采用简单类型的入参，也更加符合Java编码的最佳实践。

2.可以缓存的返回值类型，目前已支持Object实体，List以及Map。
```java
@Component
public class TestBusiness {
    @AnnoCache(seconds = 45)
    public List<TestModel>  test2(int param1, Integer param2, String param3){
        List<TestModel> list = new ArrayList<>();
        //...
        return list;
    }

    @AnnoCache(seconds = 45)
    public Map<String, TestModel> test3(int param1, Integer param2, String param3){
        Map<String, TestModel> map = new HashMap<>();
        //...
        return map;
    }
}
```

###TODO
1.内存缓存的支持。

2.Redis服务不可用等情况下的监控与降级。