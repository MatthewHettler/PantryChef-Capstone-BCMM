package com.pantrychef.pantrychef.controllers;

import com.pantrychef.pantrychef.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @Value("${filestack.api.key}")
    private String fsapi;

    @GetMapping("/")
    public String landing() {
        return "recipes/landingPage";
    }

    @GetMapping("/home")
    @ResponseBody
    public String welcome() {
        return "home";
    }

}

