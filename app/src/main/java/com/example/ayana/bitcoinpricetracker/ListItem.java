package com.example.ayana.bitcoinpricetracker;

import android.graphics.Bitmap;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

public class ListItem {

    private Bitmap mBrandIcon = null;
    private String mBrandName = null;
    private String mBrandAllName = null;

    public ListItem() {}

    public ListItem(Bitmap icon, String name, String allname) {
        mBrandIcon = icon;
        mBrandName = name;
        mBrandAllName = allname;
    }

    public void setBrandIcon(Bitmap icon) {
        mBrandIcon = icon;
    }

    public void setBrandName(String name) {
        mBrandName = name;
    }

    public void setBrandAllName(String allName) {
        mBrandAllName = allName;
    }

    public Bitmap getBrandIcon() {
        return mBrandIcon;
    }

    public String getBrandName() {
        return mBrandName;
    }

    public String getBrandAllName() {
        return mBrandAllName;
    }
}
