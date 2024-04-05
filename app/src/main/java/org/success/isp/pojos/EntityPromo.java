package org.success.isp.pojos;

//import java.time.LocalDate;
import org.joda.time.LocalDate;
import java.util.List;

public class EntityPromo {

    private int id;
    private String name;
    private String imgUrl;
    private String description;
    private LocalDate localDate;
    private String tags;

    private int likes;
    private List<Integer> whoLikes;

    public EntityPromo(String name, String imgUrl, String description, LocalDate localDate, String tags, int likes, List<Integer> whoLikes) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.description = description;
        this.localDate = localDate;
        this.tags = tags;
        this.likes = likes;
        this.whoLikes = whoLikes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Integer> getWhoLikes() {
        return whoLikes;
    }

    public void setWhoLikes(List<Integer> whoLikes) {
        this.whoLikes = whoLikes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}