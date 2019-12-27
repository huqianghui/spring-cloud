# springSecurity的安全机制

这里使用Oauth2协议。在开始之前，简单介绍Oauth一些基本概念。

OAuth2提供4种不同的角色：

* 资源所有者（Resource Owner）： 可以理解为现实世界中的终端用户，就是这里注册用户。

* 资源服务器（Resource Server）： 用户数据的一些操作（增删改查）的REST API的所在服务器

* 授权服务器（Authorization Server）： 认证服务器，验证第三方客户端是否合法。如果合法就给客户端颁布token，第三方通过token来调用资源服务器的API。

* 客户端（Client）： 调用资源服务器的发起端，这里是浏览器或者配置的某个微服务本身。

四种授权方式（Grant Type）

* anthorization_code
授权码类型，适用于Web Server Application。模式为：客户端先调用/oauth/authorize/进到用户授权界面，用户授权后返回code，客户端然后根据code和appSecret获取access token。

* implicit
简化类型，相对于授权码类型少了授权码获取的步骤。客户端应用授权后认证服务器会直接将access token放在客户端的url。客户端解析url获取token。这种方式其实是不太安全的，可以通过https安全通道和缩短access token的有效时间来较少风险。

* password
密码类型，客户端应用通过用户的username和password获access token。适用于资源服务器、认证服务器与客户端具有完全的信任关系，因为要将用户要将用户的用户名密码直接发送给客户端应用，客户端应用通过用户发送过来的用户名密码获取token，然后访问资源服务器资源。比如支付宝就可以直接用淘宝用户名和密码登录，因为它们属于同一家公司，彼此充分信任。

* client_credentials
客户端类型，是不需要用户参与的一种方式，用于不同服务之间的对接。比如自己开发的应用程序要调用短信验证码服务商的服务，调用地图服务商的服务、调用手机消息推送服务商的服务。当需要调用服务是可以直接使用服务商给的appID和appSecret来获取token，得到token之后就可以直接调用服务。

在访问授权服务的时候，使用了client_credentials。而获取access token的时候使用password方式。