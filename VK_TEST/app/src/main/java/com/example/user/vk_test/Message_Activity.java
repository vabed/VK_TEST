package com.example.user.vk_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.ArrayList;


public class Message_Activity extends Activity {
    private Button send_message;
    private EditText message;
    private ListView listView;
    ArrayList<String> inList = new ArrayList<>();
    ArrayList<String> outList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        send_message = (Button)findViewById(R.id.send_message);
        message = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView2);

        ArrayList<String> bodys = getIntent().getStringArrayListExtra("bodys");
        boolean [] states = getIntent().getBooleanArrayExtra("states");
        Log.v("count", "count:" + String.valueOf(states.length));
        listView.setAdapter(new MessageAdapter( Message_Activity.this,bodys,states));
        Intent intent = getIntent();
        final int user_id = intent.getIntExtra("id",0);

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = String.valueOf(message.getText());

                 VKRequest request = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, user_id
                         , VKApiConst.MESSAGE, text));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        System.out.println("повідомлення відправлено");
                    }
                });
            }
        });

    }
}
