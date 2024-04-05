package org.success.isp.pojos;


public class EntityMisc {
    private Integer id;
    private String name;
    private String title;
    private String content;
    private String references;

    public EntityMisc() {
    }

    public EntityMisc(String name, String title, String content, String references) {
        this.name = name;
        this.title = title;
        this.content = content;
        this.references = references;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }
}