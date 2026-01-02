package at.technikum.application.dto.users;

import at.technikum.application.enums.UserType;

import java.util.List;
import java.util.UUID;

public class UserProfile {
    private UUID id;
    private String username;
    private String email;
    private UserType usertype;
    private Integer countCreatedMedia;
    private Integer countFavs;
    private Integer countLikes;
    private Integer countRatings;
    private List<String> favGenre;

    public UserProfile(){

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUsertype() {
        return usertype;
    }

    public void setUsertype(UserType usertype) {
        this.usertype = usertype;
    }

    public Integer getCountCreatedMedia() {
        return countCreatedMedia;
    }

    public void setCountCreatedMedia(Integer countCreatedMedia) {
        this.countCreatedMedia = countCreatedMedia;
    }

    public Integer getCountFavs() {
        return countFavs;
    }

    public void setCountFavs(Integer countFavs) {
        this.countFavs = countFavs;
    }

    public Integer getCountLikes() {
        return countLikes;
    }

    public void setCountLikes(Integer countLikes) {
        this.countLikes = countLikes;
    }

    public Integer getCountRatings() {
        return countRatings;
    }

    public void setCountRatings(Integer countRatings) {
        this.countRatings = countRatings;
    }

    public List<String> getFavGenre() {
        return favGenre;
    }

    public void setFavGenre(List<String> favGenre) {
        this.favGenre = favGenre;
    }
}
