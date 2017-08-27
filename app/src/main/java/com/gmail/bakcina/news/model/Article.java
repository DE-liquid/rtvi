package com.gmail.bakcina.news.model;

public class Article {

    private long Id;
    private String Title;
    private String ImagePreview;
    private String ShareLink;

    public Article(long id, String title, String imagePreview, String shareLink) {
        Id = id;
        Title = title;
        ImagePreview = imagePreview;
        ShareLink = shareLink;
    }

    public long getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getImagePreview() {
        return ImagePreview;
    }

    public String getShareLink() {
        return ShareLink;
    }
}
