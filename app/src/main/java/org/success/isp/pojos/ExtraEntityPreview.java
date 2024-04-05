package org.success.isp.pojos;

//import java.time.LocalDate;
import org.joda.time.LocalDate;

public class ExtraEntityPreview {

    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private LocalDate localDate;

    public ExtraEntityPreview(int id, String title, String description, String imageUrl, LocalDate localDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.localDate = localDate;
    }

    public ExtraEntityPreview() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}