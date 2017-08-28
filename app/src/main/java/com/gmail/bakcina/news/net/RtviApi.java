package com.gmail.bakcina.news.net;

import com.gmail.bakcina.news.model.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Интерфейс для работы с API
 */
public interface RtviApi {

    @GET("newslist")
    Call<List<Article>> getArticles();

    @GET("newslist")
    Call<List<Article>> searchForArticles(@Query("searchString") String searchString);
}
