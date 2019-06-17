package com.jando.fitness_app.Model;

public class User {

    private String username, email, firstname, lastname, age, weight;

    public User() {

    }

    public User(String username, String email, String firstname,
                String lastname, String age, String weight) {
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.weight = weight;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String input) {
        this.username = input;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String input) {
        this.firstname = input;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String input) {
        this.lastname = input;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String input) {
        this.email = input;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String input) {
            this.age = input;
    }
    public String getWeight() {
        return weight;
    }
    public void setWeight(String input) {
            this.weight = input;
    }
}
