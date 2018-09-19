package com.enigio.blog;

import com.enigio.blog.config.CustomUserDetails;
import com.enigio.blog.entities.Role;
import com.enigio.blog.entities.User;
import com.enigio.blog.repositories.UserRepository;
import com.enigio.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class BlogApplication extends SpringBootServletInitializer {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		new BlogApplication()
				.configure(new SpringApplicationBuilder(BlogApplication.class))
				.run(args);
	}

//Добивките за лозинка се вклучуваат со инјектирање на AuthenticationManager.
// we setup the builder so that the userDetailsService is the one we coded

	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder,
									  UserRepository repository, UserService userService) throws Exception {

		if (repository.count()==0)//so save mozime poveke useri da dozvolime(isto + array.asList)
			userService.save(new User("admin", "adminPassword",
					Arrays.asList(new Role("USER"), new Role("ACTUATOR") , new Role("ADMIN"))));
		userService.save(new User("Jule", "Jule",
				Arrays.asList(new Role("USER"), new Role("ACTUATOR"), new Role("ADMIN"))));
		builder.userDetailsService(userDetailsService(repository)).passwordEncoder(passwordEncoder);
	}

	private UserDetailsService userDetailsService(final UserRepository repository) {
		return username -> new CustomUserDetails(repository.findByUsername(username));
	}//We return an instance of our CustomUserDetails
}
