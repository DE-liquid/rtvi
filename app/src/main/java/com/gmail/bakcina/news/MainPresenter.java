package com.gmail.bakcina.news;


import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.gmail.bakcina.news.model.Article;
import com.gmail.bakcina.news.net.APIRequestsHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    void initialLoad() {

        getViewState().showProgress(true);

        Call<List<Article>> call = APIRequestsHelper.createRetrofitObject().getArticles();

        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(@NonNull Call<List<Article>> call, @NonNull Response<List<Article>> response) {
                getViewState().showProgress(false);
                getViewState().showAllArticles(response.body());
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                getViewState().showProgress(false);
            }
        });
    }


}
