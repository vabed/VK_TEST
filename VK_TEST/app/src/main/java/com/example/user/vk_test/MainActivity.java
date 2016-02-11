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

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] scope = new String[]{VKScope.MESSAGES,VKScope.FRIENDS,VKScope.WALL};
    private ListView listView ;
    private Button showMessage;
    VKList list_friends;
    public static String EXTRA_MESSAGE="com.example.user.vk_test";
    public static String EXTRA_MESSAGE_1 = "com.example.user.vk_test1";
    public static String EXTRA_MESSAGE_2 = "com.example.user.vk_test2";
    public static String EXTRA_MESSAGE_3 = "com.example.user.vk_test3";
    public static String EXTRA_MESSAGE_4 = "com.example.user.vk_test4";
    private Button showStatik;
    private int friend_count;
    private int men_count = 0;
    private int online_count = 0;
    private int woman_count = 0;
    private int my_id;
    private int id_user_city;
    private int count_city = 0;
    private String user_city;
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


                        listView.setAdapter(new CustomAdapter(messages,users,MainActivity.this,list,MainActivity.this));

                    }
                });
            }
        });

        showStatik = (Button) findViewById(R.id.statistik);
        showStatik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, Statistics.class);
                intent.putExtra(EXTRA_MESSAGE, friend_count);
                intent.putExtra(EXTRA_MESSAGE_1, online_count);
                intent.putExtra(EXTRA_MESSAGE_2, woman_count);
                intent.putExtra(EXTRA_MESSAGE_3,men_count);
                intent.putExtra(EXTRA_MESSAGE_4,count_city);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                my_id = Integer.parseInt(res.userId);

                VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS,",first_name,last_name,online,city,sex"));
                VKRequest user_request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS,String.valueOf(my_id),VKApiConst.FIELDS,"city"));

                user_request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKList user_list = (VKList) response.parsedModel;
                        try {
                            id_user_city = user_list.get(0).fields.getJSONObject("city").getInt("id");
                            user_city = String.valueOf( user_list.get(0).fields.getJSONObject("city").getInt("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {

                        super.onComplete(response);
                        list_friends = (VKList) response.parsedModel;


                        friend_count = list_friends.getCount();
                        int id_friend_city;
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_expandable_list_item_1, list_friends);

                        listView.setAdapter(arrayAdapter);

                       for (int i = 0; i < list_friends.getCount(); i++) {
                            try {
                                if (Integer.parseInt(list_friends.get(i).fields.getString("online")) == 1) {
                                    online_count++;
                                }

                                if(Integer.parseInt(list_friends.get(i).fields.getString("sex")) == 2){
                                    men_count++;
                                }
                                else if (Integer.parseInt(list_friends.get(i).fields.getString("sex")) == 1){
                                    woman_count++;
                                }
                                id_friend_city = list_friends.get(i).fields.getJSONObject("city").getInt("id");
                                if (id_friend_city == id_user_city){
                                    count_city++;
                                }
                            }catch(JSONException e){
                                e.printStackTrace();
                            }catch(NullPointerException e){
                                online_count = i;
                                e.printStackTrace();
                            }
                            }

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

        intent.putExtra(EXTRA_MESSAGE,user_id);
        startActivity(intent);

    }
}
