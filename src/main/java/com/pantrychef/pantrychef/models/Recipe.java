package com.pantrychef.pantrychef.models;

import javax.persistence.*;
import java.net.UnknownServiceException;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = ("INT(11) UNSIGNED"))
    private long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(nullable = true)
    private String recipe_path;

    public Recipe(){

    }

    //Many to one annotation to User model
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    //Many to many annotation to Recipe model
    @ManyToMany
    @JoinTable(

            name = "recipes_categories",
            joinColumns = {@JoinColumn(name = "recipe_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private List<Categories> categories;

    //Many to many annotation to Ingredients model
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(

            name = "recipe_ingredients",
            joinColumns = {@JoinColumn(name = "recipe_id")},
            inverseJoinColumns = {@JoinColumn(name = "ingredient_id")}
    )
    private List<Ingredient> ingredientList;

    //Many to many annotation to Instruction model
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(

            name = "recipe_instructions",
            joinColumns = {@JoinColumn(name = "recipe_id")},
            inverseJoinColumns = {@JoinColumn(name = "instruction_id")}
    )
    private List<Instruction> instructionList;


    //Many to many annotation to User model for favorites
//    @ManyToMany(mappedBy = "favorites")
//    private List<User> favoritedBy;

    //One to many annotation to RecipeImages model
    @Column(nullable = false)
    private String recipeImageUrl;

    //One to many annotation to Comments model
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private List<Comments> comments;

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Instruction> getInstructionList() {
        return instructionList;
    }

    public void setInstructionList(List<Instruction> instructionList) {
        this.instructionList = instructionList;
    }

//    public List<User> getFavoritedBy() {
//        return favoritedBy;
//    }

//    public void setFavoritedBy(List<User> favoritedBy) {
//        this.favoritedBy = favoritedBy;
//    }

    public User getUser() { return user; }

//    public String getRecipe_path() {
//        return recipe_path;
//    }
//
//    public void setRecipe_path(String recipe_path) {
//        this.recipe_path = recipe_path;
//    }

    public void setUser(User user) { this.user = user;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public void setRecipeImageUrl(String recipeImageUrl) {
        this.recipeImageUrl = recipeImageUrl;
    }

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }
}





