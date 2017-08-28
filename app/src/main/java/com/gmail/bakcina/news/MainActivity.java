package com.gmail.bakcina.news;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private MenuItem searchItem;
    private Handler handler = new Handler();


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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        searchItem = menu.findItem(R.id.action_search);

        final android.widget.SearchView searchView = (android.widget.SearchView) searchItem.getActionView();

        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        searchView.setIconified(false);
                        searchView.requestFocus();
                        return true;
                    }
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        Log.d("Valo","onMenuItemActionCollapse");
                        searchView.setQuery("", false);
                        mainPresenter.clearSearch();
                        mainPresenter.initialLoad();
                        return true;
                    }
                });

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 1) {
                    mainPresenter.clearCommonList();
                }

                if (newText.length() > 2) {
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(() -> mainPresenter.searchNews(newText), 1500);
                }
                return true;
            }
        });

        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            if (searchItem == null) return false;
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }*/

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
    public void showAllArticles(@NonNull List<Article> data) {

        if (adapter == null) {
            adapter = new ArticleAdapter(data, this);
            articlesRV.setAdapter(adapter);
        } else {
            adapter.setData(data);
            adapter.notifyDataSetChanged();
        }
        Log.d("Valo", "showAllArticles, data -> " + data.size());
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
