package com.example.myapplication.Model;

public class WallpaperLiveModel {

    String full_img, thumb_img;

    public WallpaperLiveModel(String full_img, String thumb_img) {
        this.full_img = full_img;
        this.thumb_img = thumb_img;
    }

    public String getFull_img() {
        return full_img;
    }

    public String getThumb_img() {
        return thumb_img;
    }
}
