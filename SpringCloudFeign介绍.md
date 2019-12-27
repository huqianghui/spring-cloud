# Spring Cloud Feign介绍

常都会利用它对RestTemplate的请求拦截来实现对依赖服务的接口调用，而RestTemplate已经实现了对HTTP请求的封装处理，形成了一套模版化的调用方法。在之前的例子中，我们只是简单介绍了RestTemplate调用对实现，但是在实际开发中，由于对服务依赖对调用可能不止于一处，往往一个接口会被多处调用，所以我们通常都会针对各个微服务自行封装一些客户端累来包装这些依赖服务的调用。

这个时候我们会发现，由于RestTemplate的封装，几乎每一个调用都是简单的模版化内容。综合上述这些情况，Spring Cloud Fegin在此基础上做了进一步封装，由它来帮助我们定义和实现依赖服务接口的定义。

在Spring Cloud Feign的实现下，我们只需创建一个接口并用注解的方式来配置它，即可完成对服务提供方的接口绑定，简化了在使用Spring Cloud Ribbon时自行封装服务调用客户端的开发量。

Spring Cloud Feign具备可插拔的注解支持，包括Feign注解和JAX-RS注解。同时，为了适应Spring的广大用户，它在Netflix Feign的基础上扩展了对Spring MVC的注解支持。

Feign自身的一些主要组件，比如编码器和解码器等，它也以可插拔的方式提供，在有需求等时候我们以方便扩张和替换它们。

## Spring Cloud Feign依赖

```yml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

## 开启方法，增加@EnableFeignClients annotation

```yml
@SpringCloudApplication
@EnableFeignClients
@EnableResourceServer
public class AuthApplication {

public static void main(String[] args) {
  SpringApplication.run(AuthApplication.class, args);
}
}
```

## 使用样例

```java
@FeignClient(name = "account-service",path = "/users",configuration = FeignClientsConfiguration.class)
public interface UserServiceClient {

@RequestMapping(method = RequestMethod.GET, value = "/current", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
User getUserByName(@RequestParam("username") String username);

}
```