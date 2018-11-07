package com.enigio.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration//za da moze da go koristime Oath2
@EnableAuthorizationServer//се користи за конфигурирање на OAuth 2.0 механизмот за авторизација на серверот
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//klasa koja go implementira AuthorizationServerConfigurer
    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager);
        endpoints.tokenStore(getTokenStore());
    }//Поставување на менаџерот за проверка на автентичност на крајните точки.
    // ги дефинира овластувањата и токеничните крајни точки и токенските услуги

    @Bean
    public TokenStore getTokenStore(){
        return new InMemoryTokenStore();
    }//class InMemoryTokenStore implements TokenStore

    @Override//Поставување на клиенти со clientId, clientSecret, типови на грантови koristejki IN MEMORy configuration
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().
                withClient("my-trusted-client")//we plugIn a client ID
                .authorizedGrantTypes("client_credentials", "password")
                .authorities("ROLE_CLIENT","ROLE_TRUSTED_CLIENT").scopes("read","write","trust")
                .resourceIds("oauth2-resource").accessTokenValiditySeconds(5000).secret("secret");
    }//how long this token is going to be valid for

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.checkTokenAccess("isAuthenticated()");
    }//овде ги дефинираме безбедносните ограничувања на крајната точка на токен.
    // Treba da kreirame nash AuthenticationManager, da go @Autowired--> Checking isAuthenticated, што се враќа TRUE ако
    // корисникот не е анонимен. PROVIDE authenticationManger I go @Autowired (vo DemoAplication go konfigurirame)

}
