package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import com.example.myapplication.Model.WallpaperLiveModel;

import java.util.ArrayList;

public class Constant {

    public static String BaseApi = "http://ayttechnology.com/catiowp_panel/api/v1/api.php?get_new_wallpapers&page=1&count=15&filter=live&order=live&category=0";
    public static String POS1 = "position";

    public static String gifName = "";
    public static String gifPath = "";

    public static ArrayList<WallpaperLiveModel> LiveWallArrayList;

    public static Bitmap blurImage(Context context2, Bitmap bitmap) {
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, Math.round(((float) bitmap.getWidth()) * 0.1f), Math.round(((float) bitmap.getHeight()) * 0.1f), false);
        Bitmap createBitmap = Bitmap.createBitmap(createScaledBitmap);
        RenderScript create = RenderScript.create(context2);
        ScriptIntrinsicBlur create2 = ScriptIntrinsicBlur.create(create, Element.U8_4(create));
        Allocation createFromBitmap = Allocation.createFromBitmap(create, createScaledBitmap);
        Allocation createFromBitmap2 = Allocation.createFromBitmap(create, createBitmap);
        create2.setRadius(05.0f);
        create2.setInput(createFromBitmap);
        create2.forEach(createFromBitmap2);
        createFromBitmap2.copyTo(createBitmap);
        return createBitmap;
    }

}
