package com.enigio.blog.controllers;

import com.enigio.blog.entities.Role;
import com.enigio.blog.entities.User;
import com.enigio.blog.pojos.UserRegistration;
import com.enigio.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class UserController {//ni treba nva klasa UserRegistration
//Proveruva dali passwordite matchuvaat i dali voopshto userot postoi.
    @Autowired
    private UserService userService;

    @Autowired
    private TokenStore tokenStore;
//Helper that translates between JWT (JSON web token) encoded token values and
//Read the authentication stored under the specified token value, with its methods, or remove the same
//Signed tokens can verify the integrity of the claims contained within it, while
// encrypted tokens hide those claims from other parties.

    @PostMapping(value = "/register")
    public String register(@RequestBody UserRegistration userRegistration){
        if(!userRegistration.getPassword().equals(userRegistration.getPasswordConfirmation()))
            return "Error the two passwords do not match";
        else if(userService.getUser(userRegistration.getUsername()) != null)
            return "Error this username already exists";

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        if(pattern.matcher(userRegistration.getUsername()).find())
            return "No special characters are allowed in the username";

        userService.save(new User(userRegistration.getUsername(), userRegistration.getPassword(),
                Arrays.asList(new Role("USER"), new Role("ACTUATOR"))));
        return "User created";
    }//-	Na OAuth2 moze so ovoj user da se logirame so POST auth I da postirame post so shto koga ke gi vidime posts,
    // treba da ni razgranicuva koj creator koj post go napishal



    @GetMapping(value = "/users")
    public List<User> users(){
        return userService.getAllUsers();
    }

    @GetMapping(value = "/logouts")
    public void logout(@RequestParam (value = "access_token") String accessToken){//userot prvo ni go praka
        tokenStore.removeAccessToken(tokenStore.readAccessToken(accessToken));//go remove od Store
    }

    @GetMapping(value ="/getUsername")//ni vraka imeto na currentUser ako e authenticated, anonymousUser vo sprotivno
    public String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
