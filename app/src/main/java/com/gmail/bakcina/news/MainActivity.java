package com.gmail.bakcina.news;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.gmail.bakcina.news.model.Article;

import java.util.List;

public class MainActivity extends MvpAppCompatActivity implements MainView, ArticleAdapter.ArticleClickListener {

    RecyclerView articlesRV;

    @InjectPresenter MainPresenter mainPresenter;

    private ProgressDialog progressDialog;
    AlertDialog errorAlertDialog;
    private ArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articlesRV = (RecyclerView) findViewById(R.id.articles_rv);

        articlesRV.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        progressDialog = new ProgressDialog(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.initialLoad();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        if (errorAlertDialog != null && errorAlertDialog.isShowing()) {
            errorAlertDialog.cancel();
        }
    }

    @Override
    public void showAllArticles(List<Article> data) {
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

    @Override
    public void showErrorAlert(int messageResId) {
        errorAlertDialog =
                new AlertDialog.Builder(this)
                        .setMessage(messageResId)
                        .setPositiveButton(R.string.dialog_ok,
                                (dialogInterface, i) -> dialogInterface.cancel())
                        .create();
        errorAlertDialog.show();
    }

    private void showThrobber() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideThrobber() {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onArticleClicked(long articleId, String shareLink) {
        //todo webView to show article by link
    }
}
