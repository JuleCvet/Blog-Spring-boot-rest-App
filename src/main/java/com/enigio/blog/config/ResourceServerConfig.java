package com.enigio.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    //do resourse server go prakame access tokenot. Treba da se konfigurira I ovoj resourse server
    //serves resources that are protected by the OAth2 token
//it provides SpringSecurityAuthentication филтер koj ja implementira ovaa protekcija
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //site mozat da ja vidat ovaa strana
                .antMatchers("/","/home","/register","/login","/","/post","/post/**").permitAll()
                .antMatchers("/private/**").authenticated()
                .antMatchers("/post").authenticated()//POST ke moze da se koristi samo od authenticated users
                .antMatchers("/post/postComment").authenticated()
                .antMatchers(HttpMethod.DELETE , "/post/**").hasAuthority("ROLE_ADMIN");
    }
}
