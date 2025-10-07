package at.technikum.application.model;

public class Rating {
    String id;
    String name;
    String rating;

    public Rating() {
        this.id = "";
        this.name = "";
        this.rating = "";
    }

    public Rating(String id, String name, String rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("{id:%s, name:%s, rating:%s}", id, name, rating);
    }
}
