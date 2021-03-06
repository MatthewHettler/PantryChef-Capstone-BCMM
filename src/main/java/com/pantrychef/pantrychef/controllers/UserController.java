package com.pantrychef.pantrychef.controllers;

import com.pantrychef.pantrychef.models.Recipe;
//import com.pantrychef.pantrychef.models.ShoppingList;
import com.pantrychef.pantrychef.models.ShoppingList;
import com.pantrychef.pantrychef.models.User;
import com.pantrychef.pantrychef.repositories.RecipeRepo;
//import com.pantrychef.pantrychef.repositories.ShoppingListRepo;
import com.pantrychef.pantrychef.repositories.ShoppingListRepo;
import com.pantrychef.pantrychef.repositories.UserRepo;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class UserController {
    private UserRepo usersDao;
    private RecipeRepo recipeDao;
    private PasswordEncoder passwordEncoder;
    private ShoppingListRepo shoppingListDao;

    @Value("${filestack.api.key}")
    private String fsapi;

    public UserController(UserRepo usersDao, PasswordEncoder passwordEncoder, RecipeRepo recipeDao, ShoppingListRepo shoppingListDao) {
        this.usersDao = usersDao;
        this.passwordEncoder = passwordEncoder;
        this.recipeDao = recipeDao;
        this.shoppingListDao = shoppingListDao;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("fsapi", fsapi);
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
//        user.setProfileImageUrl("https://picsum.photos/200");
        usersDao.save(user);
//        ShoppingList myShoppingList = new ShoppingList();
//        myShoppingList.setUser(user);
//        shoppingListDao.save(myShoppingList);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", u);
        model.addAttribute("recipes", recipeDao.findAll());
        model.addAttribute("fsapi", fsapi);
        if (u.isAdmin()){
            System.out.println("this user is an admin!");
        }
        System.out.println("inside profile method!");
        return "recipes/profile";
    }

    @GetMapping("/profile/{id}")
    public String viewUserProfile(Model model, @PathVariable long id) {
            User u = usersDao.getOne(id);
            model.addAttribute("user", u);
            model.addAttribute("recipes", recipeDao.findAll());

        return "recipes/profileFromRecipe";
    }

///////////    REFERENCE GET MAPPING    /////////////
//    @GetMapping("/profile/{username}")
//    public String viewUserProfile(Model model, @PathVariable String username) {
//        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
//            User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            model.addAttribute("user", u);
//            User v = usersDao.findByUsername(username);
//            long vId = v.getId();
//            model.addAttribute("profileId", vId);
//            Recipe recipe = recipeDao.findByUserId(vId);
//            model.addAttribute("recipes", recipe);
//            model.addAttribute("user", v);
//        }
//        return "recipes/profile";
//    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable long id) {
        User u = usersDao.getOne(id);
//        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("user", admin);
        model.addAttribute("user", u);
        model.addAttribute("fsapi", fsapi);
        return "users/editProfile";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@RequestParam(name = "profileImageUrl") String profileImageUrl, @ModelAttribute User user, @PathVariable long id, Model model) {
        User updatedUser = usersDao.getOne(id);
        updatedUser.setEmail(user.getEmail());
        updatedUser.setFirst_name(user.getFirst_name());
        updatedUser.setLast_name(user.getLast_name());
        updatedUser.setUsername(user.getUsername());
//        updatedUser.setProfileImageUrl(u.getProfileImageUrl());
        updatedUser.setProfileImageUrl(profileImageUrl);
//        String hash = passwordEncoder.encode(user.getPassword());
        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", admin);
        updatedUser.setPassword(updatedUser.getPassword());
        usersDao.save(updatedUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(updatedUser, updatedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/profile";
    }

}
