package com.pantrychef.pantrychef.controllers;
<<<<<<< HEAD
import com.pantrychef.pantrychef.models.Ingredient;
import com.pantrychef.pantrychef.models.Recipe;
import com.pantrychef.pantrychef.models.User;
import com.pantrychef.pantrychef.repositories.IngredientsRepo;
import com.pantrychef.pantrychef.repositories.RecipeRepo;
import com.pantrychef.pantrychef.repositories.UserRepo;
=======
import com.pantrychef.pantrychef.models.*;
import com.pantrychef.pantrychef.repositories.*;
>>>>>>> master
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
=======

import java.security.Principal;
>>>>>>> master
import java.util.ArrayList;
import java.util.List;

@Controller
public class RecipeController {

    private RecipeRepo recipeDao;
    private UserRepo userDao;
    private IngredientsRepo ingredientsDao;
    private CategoriesRepo categoriesDao;
    @Value("${filestack.api.key}")
    private String fsapi;

<<<<<<< HEAD
    public RecipeController(RecipeRepo recipeDao, UserRepo userDao, IngredientsRepo ingredientsDao) {
=======
    public RecipeController(RecipeRepo recipeDao, UserRepo userDao, RecipeIngredientsRepo recipeIngredientsDao, IngredientsRepo ingredientsDao, CategoriesRepo categoriesDao) {
>>>>>>> master
        this.recipeDao = recipeDao;
        this.userDao = userDao;
        this.ingredientsDao = ingredientsDao;
        this.categoriesDao = categoriesDao;
    }

    @GetMapping("/recipes")
    public String getPosts(Model model){
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser"){
            User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", u);
        }else{
            model.addAttribute("recipes", recipeDao.findAll());
            return "recipes/recipes";
        }
        model.addAttribute("recipes", recipeDao.findAll());
        return "recipes/recipes";
    }

    @GetMapping("/recipes/{id}")
    public String getPost(@PathVariable long id, Model model){
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", u);
        }else{
            model.addAttribute("recipe",recipeDao.getOne(id));
            return "recipes/showRecipe";
        }
        model.addAttribute("recipe",recipeDao.getOne(id));
        return "recipes/showRecipe";
    }

    @GetMapping("/recipe/{id}/edit")
    public String editForm(@PathVariable long id, Model model) {
        Recipe recipeToEdit = recipeDao.getOne(id);
        model.addAttribute("recipe", recipeToEdit);
        model.addAttribute("fsapi", fsapi);
        return "recipes/editRecipe";
    }

    @PostMapping("/recipe/{id}/edit")
    public String updateRecipe(@ModelAttribute Recipe recipe, @PathVariable long id){
        Recipe recipeToEdit = recipeDao.getOne(id);
        recipeToEdit.setTitle(recipeToEdit.getTitle());
        recipeToEdit.setDirections(recipeToEdit.getDirections());
<<<<<<< HEAD
//        recipeToEdit.setIngredient(recipeToEdit.getIngredient());
=======
        recipeToEdit.setIngredient(recipeToEdit.getIngredient());
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipe.setUser(u);
>>>>>>> master
        recipeDao.save(recipe);
        return "redirect:/recipes";
    }

    //Create a recipe
    @GetMapping("/recipe/create")
    public String createForm(Model model){
//        User CurrentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("user", CurrentUser);
//        List<Categories> categories = new ArrayList<>();
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("fsapi", fsapi);
//        model.addAttribute("categories", categoriesDao.findAll());
        return "recipes/createRecipe";
    }

    @PostMapping("/recipe/create")
<<<<<<< HEAD
    public String createRecipe(@RequestParam(name= "ingredient-param") List<String> ingredientsStringList, @RequestParam String title, @RequestParam String directions, @RequestParam(name = "recipeImageUrl") String recipeImageUrl){
=======
    public String createRecipe(@RequestParam String title, @RequestParam String ingredients, @RequestParam String directions, @RequestParam int quantity, @RequestParam(name = "recipeImageUrl") String recipeImageUrl, @RequestParam List<Categories> categories){
>>>>>>> master
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
//        recipe.setIngredient(ingredients);
        recipe.setDirections(directions);
        recipe.setRecipeImageUrl(recipeImageUrl);
        recipe.setCategories(categories); //@RequestParam List<Categories> categories
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipe.setUser(u);
        recipeDao.save(recipe);

        List<Ingredient> recipeIngredientList = new ArrayList<>();

        for(String ingredient : ingredientsStringList){
            if(ingredient != ""){
                if(ingredientsDao.findIngredientByname(ingredient) == null) {
                    Ingredient addIngredient = new Ingredient();
                    addIngredient.setName(ingredient);
                    List<Recipe> recipeList = new ArrayList<>();
                    recipeList.add(recipe);
                    addIngredient.setRecipeList(recipeList);
                    recipeIngredientList.add(ingredientsDao.save(addIngredient));
                }else{
                    Ingredient updateIngredient = ingredientsDao.findIngredientByname(ingredient);
                    List<Recipe> recipeList = updateIngredient.getRecipeList();
                    recipeList.add(recipe);
                    updateIngredient.setRecipeList(recipeList);
                    recipeIngredientList.add(ingredientsDao.save(updateIngredient));

                }
            }
        }
        recipe.setIngredientList(recipeIngredientList);
        recipeDao.save(recipe);

        return "redirect:/recipes";
    }

    //Delete a recipe post
    @PostMapping("/recipe/{id}/delete")
    public String delete(@PathVariable long id){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUser.getId() == recipeDao.getOne(id).getUser().getId()){
            // delete post
            recipeDao.deleteById(id);
        }else{
            return "redirect:/recipes";
        }
        return "redirect:/recipes";
    }
}
