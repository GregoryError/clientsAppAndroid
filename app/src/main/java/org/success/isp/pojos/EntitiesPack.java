package org.success.isp.pojos;

import java.util.ArrayList;

public class EntitiesPack {
    private ArrayList<EntityArticle> articles;
    private ArrayList<EntityNews> news;
    private ArrayList<EntityPromo> promos;

    public EntitiesPack() {
    }

    public EntitiesPack(ArrayList<EntityArticle> articles, ArrayList<EntityNews> news, ArrayList<EntityPromo> promos) {
        this.articles = articles;
        this.news = news;
        this.promos = promos;
    }

    public ArrayList<EntityArticle> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<EntityArticle> articles) {
        this.articles = articles;
    }

    public ArrayList<EntityNews> getNews() {
        return news;
    }

    public void setNews(ArrayList<EntityNews> news) {
        this.news = news;
    }

    public ArrayList<EntityPromo> getPromos() {
        return promos;
    }

    public void setPromos(ArrayList<EntityPromo> promos) {
        this.promos = promos;
    }
}
