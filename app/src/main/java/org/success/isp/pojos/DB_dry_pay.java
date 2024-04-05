package org.success.isp.pojos;


public class DB_dry_pay {

    private Long id;
    private Long time;
    private Double cash;
    private Integer category;
    private String coment;

    private String reason;

    public DB_dry_pay(Long id, Long time, Double cash, Integer category, String coment, String reason) {
        this.id = id;
        this.time = time;
        this.cash = cash;
        this.category = category;
        this.coment = coment;
        this.reason = reason;
    }

    public DB_dry_pay() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public Long getTime() {
        return time;
    }
    public void setTime(Long time) {
        this.time = time;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}