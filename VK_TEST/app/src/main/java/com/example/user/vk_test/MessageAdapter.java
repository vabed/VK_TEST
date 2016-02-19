package com.example.user.vk_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 18.02.2016.
 */
public class MessageAdapter extends BaseAdapter {

    private ArrayList<String> bodys ;
    private boolean[] states ;

    public MessageAdapter(Context context,ArrayList<String> bodys,boolean[] states){
        this.bodys = bodys;
        this.states = states;
        this.context = context;
    }
    private Context context;
    @Override
    public int getCount() {
        return bodys.size();
    }

    @Override
    public Object getItem(int position) {
        return bodys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        TextView textView;
        if ( this.states[position]){
            view = inflater.inflate(R.layout.output,null);
            textView = (TextView)view.findViewById(R.id.output_message);

        } else {
            view = inflater.inflate(R.layout.input,null);
            textView = (TextView)view.findViewById(R.id.input_message);
        }
        textView.setText(this.bodys.get(position));
        return view;
    }
}
