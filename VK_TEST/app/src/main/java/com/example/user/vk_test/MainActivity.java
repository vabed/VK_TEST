package com.example.user.vk_test;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] scope = new String[]{VKScope.MESSAGES,VKScope.FRIENDS,VKScope.WALL};
    private ListView listView ;
    private Button showMessage;
    VKList list_friends;
    public static String EXTRA_MESSAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        VKSdk.login(this, scope);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);

        showMessage = (Button)findViewById(R.id.showMessage);
        showMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKRequest request = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, 10));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {

                        super.onComplete(response);
                        VKApiGetDialogResponse getMessagesResponse = (VKApiGetDialogResponse) response.parsedModel;

                        VKList<VKApiDialog> list = getMessagesResponse.items;

                        ArrayList<String> messages = new ArrayList<String>();
                        ArrayList<String> users = new ArrayList<String>();


                        for (VKApiDialog msg : list){
                            users.add(String.valueOf(MainActivity.this.list_friends.getById(msg.message.user_id)));
                            messages.add(msg.message.body);
                        }
                       /* ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                                android.R.layout.simple_expandable_list_item_1,messages);*/

                        listView.setAdapter(new CustomAdapter(messages,users,MainActivity.this,list,MainActivity.this));

                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {

                VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS,"first_name","last_name"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {

                        super.onComplete(response);
                        list_friends = (VKList) response.parsedModel;
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_expandable_list_item_1,list_friends);

                        listView.setAdapter(arrayAdapter);
                    }
                });
                Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            listView.setMinimumHeight(250);
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            listView.setMinimumHeight(450);
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
    public void openActivity(int user_id){
        Intent intent = new Intent(this,Message_Activity.class);
        EXTRA_MESSAGE="com.example.user.vk_test";
        intent.putExtra(EXTRA_MESSAGE,user_id);
        startActivity(intent);

    }
}
