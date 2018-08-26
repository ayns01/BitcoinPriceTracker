package com.example.ayana.bitcoinpricetracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<ListItem> {

    private int mResource;
    private List<ListItem> mItems;
    private LayoutInflater mInflater;

    /*
    * @param context
    * @param resource
    * @param items
    * */
    public ListAdapter(@NonNull Context context, int resource, @NonNull List<ListItem> items) {
        super(context, resource, items);
        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /*
     * @param position      表示するitemの位置
     * @param convertView   以前まで表示されていたview
     * @param parent        getViewで生成されたViewの親となるViewGroup
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        }else {
            view = mInflater.inflate(mResource, null);
        }

        ListItem item = mItems.get(position);

        ImageView brandicon = view.findViewById(R.id.brandicon);
        brandicon.setImageBitmap(item.getBrandIcon());

        TextView brandname = view.findViewById(R.id.brandname);
        brandname.setText(item.getBrandName());

        TextView brandallname = view.findViewById(R.id.brandallname);
        brandallname.setText(item.getBrandAllName());

        return view;
    }
}
