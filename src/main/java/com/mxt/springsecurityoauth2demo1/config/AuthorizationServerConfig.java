package com.mxt.springsecurityoauth2demo1.config;


import com.mxt.springsecurityoauth2demo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    //密码模式用到的Beanb
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    //Redis的TokenStore
    @Autowired
    @Qualifier("redisTokenStore")
    private TokenStore tokenStore;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //客户端ID
                .withClient("client")
                //密钥
                .secret(passwordEncoder.encode("112233"))
                //重定向地址
                .redirectUris("https://www.baidu.com/")
                //授权范围
                .scopes("all")
                /**
                 * 授权类型
                 * authorization_code:授权码模式
                 * password:密码模式
                 */
                .authorizedGrantTypes("authorization_code","password");
    }


    //这是密码模式需要的方法 上面是授权码模式
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService)
                .tokenStore(tokenStore);
    }
}
