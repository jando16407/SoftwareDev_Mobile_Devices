package com.jando.fitness_app.Model;

public class Exercises {

    private String desc;
    private String image;
    private int rating;

    public Exercises() {

    }

    public Exercises(String desc, String image, int rating) {
        this.desc = desc;
        this.rating = rating;
        this.image = image;
    }

    public String getDesc() { return desc; }
    public String getImage() { return image; }
    public int getRating() { return rating; }

    public String toString() {
        return this.desc + "\n\n" + "Rating: " + rating;
    }

}
