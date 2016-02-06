package com.example.user.vk_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

/**
 * Created by User on 05.02.2016.
 */
public class Message_Activity extends Activity {
    private Button send_message;
    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        send_message = (Button)findViewById(R.id.send_message);
        message = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        final int user_id = intent.getIntExtra(MainActivity.EXTRA_MESSAGE,0);
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = String.valueOf(message.getText());
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
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
