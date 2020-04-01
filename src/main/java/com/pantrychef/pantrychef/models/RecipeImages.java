package com.pantrychef.pantrychef.models;


import javax.persistence.*;

@Entity
@Table(name="recipe_images")
public class RecipeImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = ("INT(11) UNSIGNED"))
    private long id;

    @Column(nullable = false)
    private String imagePath;

    @ManyToOne
    @JoinColumn(name="recipe_id")
    private Recipe recipe;

    public RecipeImages() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
