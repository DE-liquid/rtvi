package com.gmail.bakcina.news;

import android.support.annotation.NonNull;
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

    void initialLoad() {
        if (articlesCommonList.size() > 0) {
            showArticles();
            return;
        }

        getViewState().showProgress(true);

        Call<List<Article>> call = APIRequestsHelper.createRetrofitObject().getArticles();

        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(@NonNull Call<List<Article>> call, @NonNull Response<List<Article>> response) {
                getViewState().showProgress(false);
                articlesCommonList = response.body();
                showArticles();
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                getViewState().showProgress(false);
                parseNetException(t);
            }
        });
    }

    private void showArticles() {
        getViewState().showAllArticles(articlesCommonList);
    }

    private void parseNetException(Throwable t) {
        if (t instanceof UnknownHostException) {
            getViewState().showErrorAlert(R.string.error_no_connection);
        } else {
            getViewState().showErrorAlert(R.string.error_undefined);
        }
    }


}
