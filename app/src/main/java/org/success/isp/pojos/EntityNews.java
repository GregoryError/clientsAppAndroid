package org.success.isp.pojos;

//import java.time.LocalDate;
import org.joda.time.LocalDate;
import java.util.List;

public class EntityNews {

    private Integer id;
    private String name;
    private String content;
    private String imgUrl;
    private LocalDate localDate;
    private String tags;
    private Boolean isRecent;
    private int likes;
    private List<Integer> whoLikes;


    public EntityNews(String name, String content, String imgUrl, LocalDate localDate, String tags, Boolean isRecent, int likes, List<Integer> whoLikes) {
        this.name = name;
        this.content = content;
        this.imgUrl = imgUrl;
        this.localDate = localDate;
        this.tags = tags;
        this.isRecent = isRecent;
        this.likes = likes;
        this.whoLikes = whoLikes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public Boolean getRecent() {
        return isRecent;
    }

    public void setRecent(Boolean recent) {
        isRecent = recent;
    }
}