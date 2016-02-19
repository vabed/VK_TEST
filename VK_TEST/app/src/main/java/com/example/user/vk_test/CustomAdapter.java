package com.example.user.vk_test;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CustomAdapter extends BaseAdapter {

    private ArrayList<String> users,messages;
    private Context context;
    private VKList<VKApiDialog> list;
    private MainActivity activity;


    public CustomAdapter(ArrayList<String> messages, ArrayList<String> users, Context context, VKList<VKApiDialog> list,MainActivity activity) {
        this.messages = messages;
        this.users = users;
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SetData setData = new SetData();
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.style_list_view,null);

        setData.user_name = (TextView) view.findViewById(R.id.user_name);
        setData.msg = (TextView) view.findViewById(R.id.msg);

        setData.user_name.setText(users.get(position));
        setData.msg.setText(messages.get(position));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ArrayList<String> bodys = new ArrayList<String>();

                final int id = list.get(position).message.user_id;
                VKRequest request =  new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID,id));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        try {
                            JSONArray array = response.json.getJSONObject("response").getJSONArray("items");
                            boolean[] states_of_msg = new boolean[array.length()];
                            VKApiMessage msg;
                            for (int i = 0; i < array.length(); i++) {
                                msg = new VKApiMessage(array.getJSONObject(i));
                                msg.
                                bodys.add(msg.body);
                                states_of_msg[i] = msg.out;
                            }
                            Collections.reverse(bodys);
                            Collections.reverse(Arrays.asList(states_of_msg));
                            context.startActivity(new Intent(context, Message_Activity.class).putExtra("id", id)
                                    .putExtra("bodys", bodys)
                                    .putExtra("states",states_of_msg));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //Log.v("count", "isEmpty:" + String.valueOf(messages.isEmpty()));



            }
        });

        return view;
    }
    public class SetData{
        TextView user_name,msg;

    }
}
