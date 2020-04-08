package com.pantrychef.pantrychef.controllers;

import com.pantrychef.pantrychef.models.*;
import com.pantrychef.pantrychef.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;


@Controller
public class CommentsController {
    private RecipeRepo recipeDao;
    private UserRepo userDao;
    private IngredientsRepo ingredientsDao;
    private CategoriesRepo categoriesDao;
    private CommentsRepo commentsDao;

    public CommentsController(RecipeRepo recipeDao, UserRepo userDao, IngredientsRepo ingredientsDao, CategoriesRepo categoriesDao, CommentsRepo commentsDao) {
        this.recipeDao = recipeDao;
        this.userDao = userDao;
        this.ingredientsDao = ingredientsDao;
        this.categoriesDao = categoriesDao;
        this.commentsDao = commentsDao;
    }

    @GetMapping("/comments/post/{id}")
    public String getPostCommentForm(@PathVariable Long id, Model model) {
        Recipe recipe = recipeDao.getOne(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("comment", new Comments());
        return "recipes/postComment";
    }

    @PostMapping("comments/post/{id}")
    public String postComment(@PathVariable long id, @RequestParam String comment, Model model) {
        Recipe recipe = recipeDao.getOne(id);
        model.addAttribute("recipe", recipe);
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            return "redirect:/login";
        } else if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", u);
            LocalDateTime now = LocalDateTime.now();
            Comments newComment = new Comments();
            newComment.setComment(comment);
            newComment.setCommentedAt(now);
            newComment.setUser(u);
            newComment.setRecipe(recipe);
            commentsDao.save(newComment);
            model.addAttribute("comment", newComment);
        }
        return "redirect:/recipes/" + id;
    }

    @PostMapping("comments/delete/{id}")
    public String commentToDelete(@PathVariable long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comments comment = commentsDao.getOne(id);
        Recipe recipe = recipeDao.getOne(comment.getRecipe().getId());
        if (loggedInUser.getId() == comment.getUser().getId() || loggedInUser.isAdmin()) {
            // delete post
            commentsDao.deleteById(id);
        } else {
            return "redirect:/recipes/" + recipe.getId();
        }

        return "redirect:/recipes/" + recipe.getId();
    }

    @GetMapping("/comment/{id}/edit")
    public String editForm(@PathVariable long id, Model model) {
        Comments commentToEdit = commentsDao.getOne(id);
        model.addAttribute("comment", commentToEdit);
        return "recipes/editComment";
    }

    @PostMapping("/comment/{id}/edit")
    public String updateRecipe(@PathVariable long id, Model model, @RequestParam String comment) {
        Comments commentToEdit = commentsDao.getOne(id); //@RequestParam String commented
        model.addAttribute("comment", commentToEdit);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        if (user.getId() == commentToEdit.getUser().getId()) {
            commentToEdit.setComment(comment);
            LocalDateTime now = LocalDateTime.now();
            commentToEdit.setCommentedAt(now);
            commentToEdit.setUser(user);
            commentToEdit.setRecipe(commentToEdit.getRecipe());
            commentsDao.save(commentToEdit);
        }
        return "redirect:/recipes/" + commentToEdit.getRecipe().getId();
    }
}
