package com.gmail.bakcina.news;

import com.arellomobile.mvp.MvpView;
import com.gmail.bakcina.news.model.Article;

import java.util.List;

interface MainView extends MvpView {
    void showAllArticles(List<Article> data);
    void showProgress(boolean shouldShow);
}
