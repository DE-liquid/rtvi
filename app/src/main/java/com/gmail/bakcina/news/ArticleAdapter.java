package com.gmail.bakcina.news;


import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gmail.bakcina.news.model.Article;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Article> data = new ArrayList<>();
    private ArticleClickListener articleListener;

    ArticleAdapter(List<Article> items, ArticleClickListener articleListener) {
        this.data.addAll(items);
        this.articleListener = articleListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ArticleAdapter.ViewHolder(inflater.inflate(R.layout.item_article, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article item = data.get(position);
        if (item == null) return;

        if (item.getTitle() != null) {
            holder.titleTV.setText(item.getTitle());
        }

        Glide.with(holder.itemView.getContext())
                .load(item.getImagePreview())
                .bitmapTransform(new CropCircleTransformation(holder.itemView.getContext()))
                .into(holder.previewIV);

        holder.itemView.setOnClickListener(
                TextUtils.isEmpty(item.getShareLink())
                ? null
                : view -> articleListener.onArticleClicked(item.getId(), item.getShareLink())
        );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView previewIV;
        TextView titleTV;

        ViewHolder(final View itemView){
            super(itemView);
            previewIV = (ImageView) itemView.findViewById(R.id.preview_iv);
            titleTV = (TextView )itemView.findViewById(R.id.title_tv);
        }
    }

    public void setData(List<Article> newData) {
        this.data.clear();
        this.data.addAll(newData);
    }

    public interface ArticleClickListener{
        void onArticleClicked(long articleId, String shareLink);
    }

}
