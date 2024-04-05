package org.success.isp.api;

import android.content.Intent;

import org.success.isp.pojos.AppInitData;
import org.success.isp.pojos.DB_dry_pay;
import org.success.isp.pojos.EntitiesPack;
import org.success.isp.pojos.EntityArticle;
import org.success.isp.pojos.EntityMessage;
import org.success.isp.pojos.EntityMisc;
import org.success.isp.pojos.EntityNews;
import org.success.isp.pojos.EntityPromo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @GET("getInitial")
    Call<AppInitData> getInitial(@Header("name") String name,
                                 @Header("pass") String pass);

    @GET("getPayments")
    Call<List<DB_dry_pay>> getPayments(@Header("uid") String uid,
                                       @Header("pass") String pass);

    @GET("getTempPayText")
    Call<EntityMisc> getTempPayText();


    @GET("getTrustedPay")
    Call<String> getTrustedPay(@Header("uid") String uid,
                               @Header("pass") String pass);

    @GET("getMessages")
    Call<ArrayList<EntityMessage>> getMessages(@Header("uid") String uid,
                                               @Header("pass") String pass);

    @POST("setMessage")
    Call<String> setMessage(@Header("uid") String uid,
                            @Header("pass") String pass,
                            @Body EntityMessage entityMessage);

    @POST("getInfoByTags")
    Call<EntitiesPack> getInfoByTags(@Body ArrayList<String> tags);


    @POST("setToken")
    Call<String> setToken(@Header("uid") String uid,
                          @Header("pass") String pass,
                          @Body String token);

    @GET("getNews")
    Call<List<EntityNews>> getNews();

    @GET("getArticles")
    Call<List<EntityArticle>> getArticles();

    @GET("getPromos")
    Call<List<EntityPromo>> getPromos();

    @POST("likeNews")
    Call<String> likeNews(@Header("uid") String uid,
                          @Header("pass") String pass,
                          @Body Integer newsId);

    @POST("likePromo")
    Call<String> likePromo(@Header("uid") String uid,
                           @Header("pass") String pass,
                           @Body Integer promoId);

    @POST("likeArticle")
    Call<String> likeArticle(@Header("uid") String uid,
                             @Header("pass") String pass,
                             @Body Integer articleId);

}















