# shiro-spring-boot-starter
为Shiro适配Spring Boot. 注意, 默认配置是RESTful服务版本的shiro, 即无session


## 可用配置
```
shiro.realm= # realm类
shiro.loginUrl=
shiro.successUrl=
shiro.unauthorizedUrl=
shiro.filterChainDefinitions.{过虑器名}={URL路径}
```

## maven引入
```
    <dependency>
        <groupId>cn.fh</groupId>
        <artifactId>shiro-spring-boot-starter</artifactId>
        <version>1.0.0</version>
    </dependency>
```
