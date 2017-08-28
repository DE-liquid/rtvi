package com.gmail.bakcina.news;

import android.support.annotation.NonNull;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.gmail.bakcina.news.model.Article;
import com.gmail.bakcina.news.net.APIRequestsHelper;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private List<Article> articlesCommonList = new ArrayList<>();
    private List<Article> articlesLastSearched = new ArrayList<>();
    private boolean isInitialized = false;

    private Call<List<Article>> searchCall;

    void initialLoad() {

        if (isInitialized) {
            if (articlesLastSearched.size() > 0) {
                showArticles(articlesLastSearched);
                return;
            }
            if (articlesCommonList.size() > 0) {
                showArticles(articlesCommonList);
                return;
            }
        }


        getViewState().showProgress(true);

        Call<List<Article>> call = APIRequestsHelper.createRetrofitObject().getArticles();

        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(@NonNull Call<List<Article>> call, @NonNull Response<List<Article>> response) {
                getViewState().showProgress(false);
                articlesCommonList = response.body();
                showArticles(articlesCommonList);
                isInitialized = true;
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                getViewState().showProgress(false);
                parseNetException(t);
            }
        });
    }

    private void showArticles(List<Article> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        getViewState().showAllArticles(data);
    }

    private void parseNetException(Throwable t) {
        if (t instanceof UnknownHostException) {
            getViewState().showErrorAlert(R.string.error_no_connection);
        } else {
            getViewState().showErrorAlert(R.string.error_undefined);
        }
    }

    public void searchNews(String searchQuery) {
        Log.d("Valo", "searchNews -> " + searchQuery);

        if (searchCall != null && searchCall.isExecuted()) {
            searchCall.cancel();
        }

        searchCall = APIRequestsHelper.createRetrofitObject().searchForArticles(searchQuery);
        getViewState().showProgress(true);
        searchCall.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(@NonNull Call<List<Article>> call, @NonNull Response<List<Article>> response) {
                getViewState().showProgress(false);
                articlesLastSearched = response.body();
                showArticles(response.body());
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                getViewState().showProgress(false);
                parseNetException(t);
            }
        });
    }

    void clearCommonList() {
        showArticles(null);
    }

    void clearSearch() {
        articlesLastSearched.clear();
    }


}
