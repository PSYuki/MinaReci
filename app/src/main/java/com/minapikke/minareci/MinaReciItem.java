package com.minapikke.minareci;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Yuki on 2015/09/22.
 */
public class MinaReciItem {

    private String reciTitle;
    private Bitmap reciImage;
    private String reciUrl;

    public String getReciTitle() {
        return reciTitle;
    }

    public void setReciTitle(String reciTitle) {
        this.reciTitle = reciTitle;
    }

    public Bitmap getReciImage() {
        return reciImage;
    }

    public void setReciImage(Bitmap reciImage) {
        this.reciImage = reciImage;
    }

    public String getReciUrl() {
        return reciUrl;
    }

    public void setReciUrl(String reciUrl) {
        this.reciUrl = reciUrl;
    }
}
