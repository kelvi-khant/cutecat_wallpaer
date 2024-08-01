package com.example.myapplication.Model;

public class WallpaperModel {

    private int id ;
    private String wallpaperUrl;

    public WallpaperModel(int id, String wallpaperUrl) {
        this.id = id;
        this.wallpaperUrl = wallpaperUrl;
    }

    public WallpaperModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWallpaperUrl() {
        return wallpaperUrl;
    }

    public void setWallpaperUrl(String wallpaperUrl) {
        this.wallpaperUrl = wallpaperUrl;
    }

    @Override
    public String toString() {
        return "Wallpaper{" +
                "id=" + id +
                ", imageUrl='" + wallpaperUrl + '\'' +
                '}';
    }
}
