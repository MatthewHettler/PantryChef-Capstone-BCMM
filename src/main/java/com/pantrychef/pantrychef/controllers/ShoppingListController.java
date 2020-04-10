package com.pantrychef.pantrychef.controllers;

import com.pantrychef.pantrychef.models.ShoppingList;
import com.pantrychef.pantrychef.models.User;
import com.pantrychef.pantrychef.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ShoppingListController {
    private RecipeRepo recipeDao;
    private UserRepo userDao;
    private IngredientsRepo ingredientsDao;
    private CategoriesRepo categoriesDao;
    private CommentsRepo commentsDao;
    private ShoppingListRepo shoppingListDao;
    @Value("${filestack.api.key}")
    private String fsapi;

    public ShoppingListController(RecipeRepo recipeDao, UserRepo userDao, IngredientsRepo ingredientsDao, CategoriesRepo categoriesDao, CommentsRepo commentsDao, ShoppingListRepo shoppingListDao) {
        this.recipeDao = recipeDao;
        this.userDao = userDao;
        this.ingredientsDao = ingredientsDao;
        this.categoriesDao = categoriesDao;
        this.commentsDao = commentsDao;
        this.shoppingListDao = shoppingListDao;
    }

    @GetMapping("/list")
    public String getShoppingList(Model model){
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getUserById(loggedIn.getId());
        ShoppingList shoppingList = shoppingListDao.getShoppingListByUserId(user.getId());
        model.addAttribute("user", user);
        return "users/shoppingList";
    }

    @GetMapping("/list/addItem")
    public String getAddItem(Model model){
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getUserById(loggedIn.getId());
        ShoppingList shoppingList = new ShoppingList();
        model.addAttribute("user", user);
        model.addAttribute("shoppingList", shoppingList);
        return "users/editShoppingList";
    }

    @PostMapping("/list/addItem")
    public String postAddItem(@ModelAttribute ShoppingList shoppingList, Model model, @RequestParam String shoppingListItem){
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getUserById(loggedIn.getId());
        shoppingList.setUser(user);
        shoppingList.setShoppingListItem(shoppingListItem);
        model.addAttribute("user", user);
        model.addAttribute("shoppingList", shoppingList);
        shoppingListDao.save(shoppingList);
        return "redirect:/list";
    }
}
