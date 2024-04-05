package org.success.isp.pojos;

//import java.time.LocalDate;
import org.joda.time.LocalDate;
import java.util.List;

public class EntityArticle {

    private int id;
    private String name;
    private LocalDate localDate;
    private Integer likes;
    private String tags;
    private List<String> headers;
    private List<String> paragraphs;
    private List<String> imgUrls;
    private Boolean isRecent;
    private List<Integer> whoLikes;
    private List<Integer> itemSequence;

    public EntityArticle() {
    }

    public EntityArticle(String name, LocalDate localDate, Integer likes, String tags, List<String> headers, List<String> paragraphs, List<String> imgUrls, Boolean isRecent, List<Integer> whoLikes, List<Integer> itemSequence) {
        this.name = name;
        this.localDate = localDate;
        this.likes = likes;
        this.tags = tags;
        this.headers = headers;
        this.paragraphs = paragraphs;
        this.imgUrls = imgUrls;
        this.isRecent = isRecent;
        this.whoLikes = whoLikes;
        this.itemSequence = itemSequence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<String> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public Boolean getRecent() {
        return isRecent;
    }

    public void setRecent(Boolean recent) {
        isRecent = recent;
    }

    public List<Integer> getItemSequence() {
        return itemSequence;
    }

    public void setItemSequence(List<Integer> itemSequence) {
        this.itemSequence = itemSequence;
    }
}