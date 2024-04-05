package org.success.isp.pojos;

import java.util.List;

public class AppInitData {
    // User general billing data:
    private Integer uid;
    private String payDay;
    private String planName;
    private Float balance;
    private Float currentBalance;
    private String state;

    // Extra data:
    private List<ExtraEntityPreview> articles;
    private List<ExtraEntityPreview> news;
    private List<ExtraEntityPreview> promos;

    public AppInitData() {
    }

    public AppInitData(Integer uid, String payDay, String planName, Float balance, Float currentBalance, String state, List<ExtraEntityPreview> articles, List<ExtraEntityPreview> news, List<ExtraEntityPreview> promos) {
        this.uid = uid;
        this.payDay = payDay;
        this.planName = planName;
        this.balance = balance;
        this.currentBalance = currentBalance;
        this.state = state;
        this.articles = articles;
        this.news = news;
        this.promos = promos;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPayDay() {
        return payDay;
    }

    public void setPayDay(String payDay) {
        this.payDay = payDay;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Float getBalance() {
        return balance;
    }

    public Float getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Float currentBalance) {
        this.currentBalance = currentBalance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ExtraEntityPreview> getArticles() {
        return articles;
    }

    public void setArticles(List<ExtraEntityPreview> articles) {
        this.articles = articles;
    }

    public List<ExtraEntityPreview> getNews() {
        return news;
    }

    public void setNews(List<ExtraEntityPreview> news) {
        this.news = news;
    }

    public List<ExtraEntityPreview> getPromos() {
        return promos;
    }

    public void setPromos(List<ExtraEntityPreview> promos) {
        this.promos = promos;
    }
}
