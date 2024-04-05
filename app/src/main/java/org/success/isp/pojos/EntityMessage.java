package org.success.isp.pojos;;

public class EntityMessage {
    private String text;
    private Long time;
    private Boolean fromUser;
    private String extra;
    public EntityMessage() {
        fromUser = false;
    }

    public EntityMessage(String text, Long time, String extra, Boolean fromUser) {
        this.text = text;
        this.time = time;
        this.fromUser = fromUser;
        this.extra = extra;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Boolean getFromUser() {
        return fromUser;
    }

    public void setFromUser(Boolean fromUser) {
        this.fromUser = fromUser;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
