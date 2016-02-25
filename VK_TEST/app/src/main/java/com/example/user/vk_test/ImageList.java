package com.example.user.vk_test;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vk.sdk.api.model.VKList;

import org.json.JSONException;


public class ImageList extends BaseAdapter {
    private final Activity context;
    private VKList list_friends;
    public ImageList(Activity context,VKList list_friends) {
        Log.v("creating",String.valueOf(list_friends.size()));
        this.context = context;
        this.list_friends = list_friends;

    }

    @Override
    public int getCount() {
        return list_friends.size();
    }

    @Override
    public Object getItem(int position) {
        return list_friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.image_layout, null, true);
        TextView textView = (TextView)view.findViewById(R.id.txt);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        String name = list_friends.get(position).toString();
        Log.v("message1",String.valueOf(list_friends.size()));
        try {
            String image = list_friends.get(position).fields.getString("photo_50");
            new DownloadImageTask(imageView).execute(image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
         textView.setText(name);

        return view;
    }
}
