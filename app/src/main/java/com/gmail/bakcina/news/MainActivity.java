package com.gmail.bakcina.news;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.gmail.bakcina.news.model.Article;

import java.util.List;

public class MainActivity extends MvpAppCompatActivity implements MainView, ArticleAdapter.ArticleClickListener {

    RecyclerView articlesRV;

    @InjectPresenter MainPresenter mainPresenter;

    private ProgressDialog progressDialog;
    private ArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articlesRV = (RecyclerView) findViewById(R.id.articles_rv);

        articlesRV.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.initialLoad();
    }

    @Override
    public void showAllArticles(List<Article> data) {
        Log.d("Valo", "data -> " + data);
        if (data == null) return;

        adapter = new ArticleAdapter(data, this);
        articlesRV.setAdapter(adapter);

    }

    @Override
    public void showProgress(boolean shouldShow) {
        if (shouldShow) {
            showThrobber();
        } else {
            hideThrobber();
        }
    }

    private void showThrobber() {
        Log.d("Valo", "show throbber");
        // TODO: 26.08.2017 show loading throbber
    }

    private void hideThrobber() {
        Log.d("Valo", "hide throbber");
        // TODO: 26.08.2017 hode throbber
    }

    @Override
    public void onArticleClicked(long articleId, String shareLink) {
        //todo webView to show article by link
    }
}
