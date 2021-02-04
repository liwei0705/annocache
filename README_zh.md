# annocache

#### 介绍
annocache-spring-boot-starter是一个超轻量级的注解式API缓存组件，只需要一行代码，实现特定函数的返回值的自动化缓存。

具备二级缓存功能，在并发访问时只允许一个线程进行缓存内容的更新，其余访问线程从二级缓存获取最后一个版本的缓存值。

#### 使用说明
参考示例项目annocache-sample-api。

1.pom.xml中增加依赖。

2.目前支持Redis缓存方式，需在application.yml中指定Redis服务器的地址和端口。

3.在需要缓存的方法上添加 @AnnoCache(seconds = 45) 注解代码即可实现功能。seconds为缓存时长，单位为秒。

4.添加缓存注解的方法参数不要为Object型，所有参数都会参与缓存Key的构建。

5.可以缓存的返回值类型，目前支持Object实体，或者List，或者Map。