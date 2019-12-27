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
