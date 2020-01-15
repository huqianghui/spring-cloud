# Exercise #1 Run the SpringMVC app

1. Check development environment by using PowerShell command.

```shell
javac -version
```

```shell
mvn -version
```

How to install Maven: https://maven.apache.org/install.html

2. Download the sample code from Github repository and open it by using Visual Studio Code

Git clone from the repo

```
cd .\springmvc-monolithic-application
```

```
mvn clean install
```

```
cd .\target
java -jar .\account-service.jar
```

3. Build and run the application, open browser to testify the webpage

Notice：SpringBoot 自带 Tomcat，应用运行于 Tomcat 中

# Exercise #2 Remode the SpringMVC app to SpringCloud

1)Replace the original code by using exercise #2 sample code.

打开 Ex2 的脚手架文件作为初始框架，以后可以从 Spring.io 生成脚手架工程
Notice：这里的 annotation 是 SpringCloud，不是 SpringBoot 了
配置 Parent Pom.xml 增加 function model
在 resource 下修改 application.yml

拆分 Authoration 服务：

1. 复制一个工程作为 Auth-Service
2. 修改 Pom.xml 增加 MongoDB 依赖
3. 从 SpringMVC 应用中 copy 对应代码
4. 修改 Pom.xml 增加 Sping Security 相关依赖
5. 修改 domain/User.java 增加 import
6. 新建 service/ feignClient 组件和 resttemplate 调用 account-service
7. 修改 config/ OAuthConfig 代码
8. 在启动类上增加 feignclient 声明
9. 修改 spring config server/auth-service.yml, 增加端口定义

从 Account-Service：

1. 删除 config 文件夹
2. 修改 spring config server/account-service.yml

2) Change the configuration and modify front end code following instruction.
   拆分前后端

1. 增加 gateway 作为前端静态资源部署（以后建议使用 ngix 部署）
2. 修改 Pom 增加 Spring Security 依赖
3. 在 src/main/新建 Config 文件夹，把 ResourceServerConfig 类拷贝到 config 目录下，修改增加一些 rest 相关的 bean
4. 在调用远端认证服务时，需要转发一些参数传递进入认证服务器，所以新创建一个 CustomUserInfoTokenServices.java
5. 修改启动类，增加申明
6. 在 spring config server 的 application.yml，增加 gateway 的配置为 gateway.yaml

3) Build and run the microservices project by using Maven
   、、、
   cd step2
   mvn clean install
   、、、
   按顺序启动服务
   cd 到对应服务的 target 目录下
   java -jar

Spring Config Server
Eureka-server
auth-service
account-service
gateway

4. Open the browser to testify the application
   localhost:10000

5. Open the browser to check the local SpringCloud dashboard
   check eureka-service
   localhost:

Best practice, add Spring-Boot Admin

# 对已经存在单体应用进行微服务拆分步骤

一个简单应用用 account-service 作为例子，业务比较简单就是用户注册登陆以及转户数据的记录与统计展示。

1. 注册用户
   ![createUserAndAccount](./images/createUserAndAccount.gif)
2. 创建账户信息
   ![createAccountInfo](./images/createAccountInfo.gif)
3. 登出与登录
   ![logoutAndLogin](./images/logoutAndLogin.gif)

在这个例子里面，为了简单和减少依赖性，使用的 mongoDB 的内存数据库。包括配置和 token 等，都使用内存存储。
作为学习代码，不可用于生产。

从现在工程来展示这个业务可以分成三部分：

- 1.前端页面，负责展示部分
- 2.账户信息管理
- 3.非功能性，用户登陆与注册，认证等。

![code structure](./images/codeStructure.png)

对于微服务的拆分具体的理论指导等，请参照另外一个文档。

如图是在一个真实的逻辑场景架构图：

![realWorldFrameworkDesignPicture](./images/realWorldFrameworkDesignPicture.png)

在这个实际例子里面，我们主要做到如下两步：

1） 做到前后端分离，把静态资源剥离出来，单独存放与发布

2） 把用户认证这个非功能性功能单独剥离出来，实现 SSO 等。

3） 剩下的用户和账户信息的管理维护作为一个服务。

如下图实现一个最简单，基于 spring cloud 的微服务体系。
![studyFrameworkDesignPicture](./images/studyFrameworkDesignPicture.png)

按照这个架构图，需要三个功能：
1） API gateway，当做资源服务器，同时也是姿态资源服务，作为整个服务的入口。
2） 认证服务，提供 token 的颁发和认证。
3） 用户和账户的管理，功能性服务。

这里提供了三个模板工程，看怎么一步步实现拆分。由于功能模块变多，使用同一的 parent 来指定 maven 依赖版本和共同依赖组件。

功能如下：
![springCloudExampleProjects](./images/springCloudExampleProjects.png)

parent pom

属性执行和版本规定：

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <spring-cloud.version>Greenwich.SR3</spring-cloud.version>
    <java.version>1.8</java.version>
</properties>

<dependencyManagement>
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring-cloud.version}</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencies>
</dependencyManagement>
```

公共依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

功能模块：

```xml
<modules>
    <module>gateway</module>
    <module>auth-service</module>
    <module>account-service</module>
    <module>spring-config-server</module>
    <module>eureka-server</module>
</modules>
```

增加共同配置，增加 application.yml

```yml
logging:
  level:
    org.springframework.security: INFO

hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 300000

feign:
  hystrix:
    enabled: true

eureka:
  instance:
    prefer-ip-address: true
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/

management: #actuator
  endpoints:
    enable: true
    web:
      exposure:
        include: "*"
```

## 非功能模块介绍

1. 因为是微服务，如果要跑起来最少需要一个服务注册发现。所以在进行拆分之前我们先引入 eureka。
   **请参照 eureka 的说明**

2. 为了集中配置，我们也使用了 springconfig
   **请参照 Spring cloud server 的说明**

3. 为了提供统一的认证和服务流量转发，我们使用 spring cloud zuul 作为网关
   **请参照 spring cloud zuul 说明**

4. 在拆分之前，我们也要稍微了解一下 spring security 相关知识
   **请参照 spring security 说明**

从 git 上获取到基础代码，这些代码都可以直接跑，但是还没有业务代码。

## 服务的实际拆分步骤

按照上面说的三步,根据依赖关系我们倒过来修改：

3） 用户和账户的管理，功能性服务。

step1）增加依赖
maven 依赖中增加内存 mongoDB 相关的依赖

```xml
<profiles>
        <profile>
            <id>local-db</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <dependencies>
                <dependency>
                    <groupId>de.flapdoodle.embed</groupId>
                    <artifactId>de.flapdoodle.embed.mongo</artifactId>
                    <version>1.50.3</version>
                </dependency>
            </dependencies>
        </profile>
    </profiles>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>
        <dependency>
            <groupId>org.codehaus.jackson</groupId>
            <artifactId>jackson-core-asl</artifactId>
            <version>1.9.13</version>
        </dependency>
    </dependencies>
```

step2） 用户和账户服务的把 config 配置信息和 security 信息去掉，其余的全部拷贝到目录下面去掉。
但是需要修改所有的 namespace。最后结果如下图：

![accountServiceCodeStructure](./images/accountServiceCodeStructure.png)

step3) 增加配置文件 account-service.yml

```yml
server:
  port: 9300
```

2） 认证服务，提供 token 的颁发和认证。

step1） 增加 maven 依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-oauth2</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
</dependencies>
```

step2) 创建 user 作为认证信息用

```java
package com.seattle.msready.auth.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class User implements UserDetails {

private String username;

private String password;

@Override
public String getPassword() {
    return password;
}

@Override
public String getUsername() {
    return username;
}

@Override
public List<GrantedAuthority> getAuthorities() {
    return null;
}

public void setUsername(String username) {
    this.username = username;
}

public void setPassword(String password) {
    this.password = password;
}

@Override
public boolean isAccountNonExpired() {
    return true;
}

@Override
public boolean isAccountNonLocked() {
    return true;
}

@Override
public boolean isCredentialsNonExpired() {
    return true;
}

@Override
public boolean isEnabled() {
    return true;
}
}
```

step3) 需要远程调用 account-service 的用户信息，创建一个 feignClient

```java
package com.seattle.msready.auth.service.security;

import com.seattle.msready.auth.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service",path = "/users",configuration = FeignClientsConfiguration.class)
public interface UserServiceClient {

@RequestMapping(method = RequestMethod.GET, value = "/current", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
User getUserByName(@RequestParam("username") String username);

}
```

step4）因为用户信息是存放在 account-service 服务中的，所以用户的获取需要通过 rest 替换 DB 查找
所以新建一个 RestUserDetailsService

```java
package com.seattle.msready.auth.service.security;

import com.seattle.msready.auth.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RestUserDetailsService implements UserDetailsService {

    @Autowired
    UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User remoteUser= userServiceClient.getUserByName(username);

        return remoteUser;
    }
}
```

step5) 把 OAuth2AuthorizationConfig 和 WebSecurityConfig 拷贝过来，做相应的修改

```java
package com.seattle.msready.auth.config;

import com.seattle.msready.auth.service.security.RestUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestUserDetailsService restUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf().disable().
                authorizeRequests()
                .antMatchers("/actuator/**")
                .permitAll()
                .anyRequest().authenticated();
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(restUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
```

```java
package com.seattle.msready.auth.config;

import com.seattle.msready.auth.service.security.RestUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private TokenStore tokenStore = new InMemoryTokenStore();

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private RestUserDetailsService userDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // @formatter:off
        clients.inMemory()
                .withClient("browser")
                .autoApprove("ui");
        // @formatter:on
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(new BCryptPasswordEncoder());
    }

}
```

在启动类上增加 feignclient 的申明

```java
package com.seattle.msready.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringCloudApplication
@EnableFeignClients
@EnableResourceServer
public class AuthApplication {

public static void main(String[] args) {
    SpringApplication.run(AuthApplication.class, args);
}

}

```

step5） 增加 config，增加 auth-service.yml

```yml
server:
  port: 5000
```

1） API gateway，当做资源服务器，同时也是静态资源服务，作为整个服务的入口。

step1): 把单体应用 resoucre 下的 static 目录拷贝到 gateway 的 resource 下面
![staticResource](./images/staticResource.png)

step2): 把 gateway 作为 resource server

1. 增加 security 的依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-oauth2</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>
```

2. 新建 config 目录，把 ResourceServerConfig 类拷贝到 config 目录下。

在调用远端认证服务时，需要转发一些参数传递进入认证服务器，所以新创建一个 CustomUserInfoTokenServices

```java
package com.seattle.msready.gateway.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.*;

public class CustomUserInfoTokenServices implements ResourceServerTokenServices {

protected final Log logger = LogFactory.getLog(getClass());

private static final String[] PRINCIPAL_KEYS = new String[] { "user", "username",
        "userid", "user_id", "login", "id", "name" };

private final String userInfoEndpointUrl;

private final String clientId;

private OAuth2RestOperations restTemplate;

private String tokenType = DefaultOAuth2AccessToken.BEARER_TYPE;

private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();

public CustomUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
    this.userInfoEndpointUrl = userInfoEndpointUrl;
    this.clientId = clientId;
}

public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
}

public void setRestTemplate(OAuth2RestOperations restTemplate) {
    this.restTemplate = restTemplate;
}

public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
    this.authoritiesExtractor = authoritiesExtractor;
}

@Override
public OAuth2Authentication loadAuthentication(String accessToken)
        throws AuthenticationException, InvalidTokenException {
    Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);
    if (map.containsKey("error")) {
        this.logger.debug("userinfo returned error: " + map.get("error"));
        throw new InvalidTokenException(accessToken);
    }
    return extractAuthentication(map);
}

private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
    logger.debug("map:" + map);
    Object principal = getPrincipal(map);
    OAuth2Request request = getRequest(map);
    List<GrantedAuthority> authorities = this.authoritiesExtractor
            .extractAuthorities(map);
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            principal, "N/A", authorities);
    token.setDetails(map);
    return new OAuth2Authentication(request, token);
}

private Object getPrincipal(Map<String, Object> map) {
    for (String key : PRINCIPAL_KEYS) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
    }
    return "unknown";
}

@SuppressWarnings({ "unchecked" })
private OAuth2Request getRequest(Map<String, Object> map) {
    Map<String, Object> request = (Map<String, Object>) map.get("oauth2Request");

    String clientId = (String) request.get("clientId");
    Set<String> scope = new LinkedHashSet<>(request.containsKey("scope") ?
            (Collection<String>) request.get("scope") : Collections.<String>emptySet());

    return new OAuth2Request(null, clientId, null, true, new HashSet<>(scope),
            null, null, null, null);
}

@Override
public OAuth2AccessToken readAccessToken(String accessToken) {
    throw new UnsupportedOperationException("Not supported: read access token");
}

@SuppressWarnings({ "unchecked" })
private Map<String, Object> getMap(String path, String accessToken) {
    this.logger.debug("Getting user info from: " + path);
    try {
        logger.debug("accessToken: " + accessToken);
        OAuth2RestOperations restTemplate = this.restTemplate;
        if (restTemplate == null) {
            BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
            resource.setClientId(this.clientId);
            restTemplate = new OAuth2RestTemplate(resource);
            this.restTemplate= restTemplate;
        }
        OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext()
                .getAccessToken();
        if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
            DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(
                    accessToken);
            token.setTokenType(this.tokenType);
            restTemplate.getOAuth2ClientContext().setAccessToken(token);
        }
        return restTemplate.getForEntity(path, Map.class).getBody();
    }
    catch (Exception ex) {
        this.logger.info("Could not fetch user details: " + ex.getClass() + ", "
                + ex.getMessage());
        return Collections.<String, Object>singletonMap("error",
                "Could not fetch user details");
    }
}
}
```

因为作为 resourceServer 的 gateway 需要和认证服务远程调用，所以需要增加一些 rest 相关的 bean。
完整代码如下：

```java
package com.seattle.msready.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final ResourceServerProperties sso;

    @Autowired
    public ResourceServerConfig(ResourceServerProperties sso) {
        this.sso = sso;
    }

    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    public OAuth2RestTemplate clientCredentialsRestTemplate() {
        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
    }

    @Bean
    public ResourceServerTokenServices tokenServices() {
        CustomUserInfoTokenServices customUserInfoTokenServices = new CustomUserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
        customUserInfoTokenServices.setRestTemplate(clientCredentialsRestTemplate());
        return customUserInfoTokenServices;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth/**","/actuator/**","/accounts/").permitAll()
                .antMatchers("/","/favicon.ico","/js/**","/css/**","/fonts/**","/images/**","/*.html").permitAll()
                .anyRequest().authenticated();
    }
}
```

step3)修改启动类，增加申明

```java
package com.seattle.msready.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringCloudApplication
@EnableZuulProxy
@EnableOAuth2Client
public class GatewayApplication {

public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
}
}
```

step4):为了满足静态资源的请求路由，已经认证服务的配置，需要在 spring config server 里面，增加 gateway 的配置为 gateway.yaml

```yaml
zuul:
  ignoredServices: "*"
  host:
    connect-timeout-millis: 300000
    socket-timeout-millis: 300000
  routes:
    auth-service:
      path: /oauth/**
      serviceId: auth-service
      stripPrefix: false
      sensitiveHeaders:
    account-service:
      path: /accounts/**
      serviceId: account-service
      stripPrefix: false
      sensitiveHeaders:
security:
  oauth2:
    client:
      clientId: gateway
      accessTokenUri: http://localhost:5000/oauth/token
      grant-type: client_credentials
      scope: server
    resource:
      user-info-uri: http://localhost:5000/users/current

server:
  port: 4000

hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 300000

ribbon:
  ReadTimeout: 300000
  ConnectTimeout: 300000
```

# Exercise #3 Deploy to Azure SpringCloud

1. Replace the code by using exercise #3 sample code.
2. Delete the original config server code.
3. Create Azure SpringCloud by using Azure Pass Account if they don’t have an active Azure subscription
4. Deploy services Jar to Azure SpringCloud by using Azure CLI
5. Check the services status on Azure SpringCloud portal
6. Create Azure Website
7. Modify front-end configuration and deploy to Azure Website.
8. Open the browser to testify the application
