package io.apptizer.nsdclientapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.apptizer.nsdclientapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ClientActivity";

    private ActivityMainBinding activityMainBinding;
    private String mServiceName = "BIZ_1whe199y29f_PA";
    private final String SERVICE_TYPE = "_http._tcp.";
    private Socket socket;
    private List<Order> orderList = new ArrayList<>();
    private Gson gson = new Gson();
    private Context context;

    Handler updateConversationHandler;
    NsdServiceInfo nsdServiceInfo = null;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        Intent serverMessageService = new Intent(this.getApplicationContext(), ServerMessageService.class);
        serverMessageService.putExtra("mServiceType", SERVICE_TYPE);
        serverMessageService.putExtra("mServiceName", mServiceName);
        this.getApplicationContext().startService(serverMessageService);
        context = this.getApplicationContext();


        IntentFilter filter = new IntentFilter();
        filter.addAction("update.content");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String jsonString = intent.getStringExtra("messageContent");
//                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//                System.out.println(jsonString);
//                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                OrderResponse orderResponse = gson.fromJson(jsonString, OrderResponse.class);

                List<Order> filteredOrderList = new ArrayList<>();

                if (orderResponse.getOrders().size() > 0) {
                    activityMainBinding.latestOrderContainer.setVisibility(View.VISIBLE);
                    activityMainBinding.latestOrderId.setText(orderResponse.getOrders().get(orderResponse.getOrders().size() - 1).getOrderId());
                    if (orderResponse.getOrders().size() > 1) {
                        for (int i = 0; i < orderResponse.getOrders().size() - 1; i++) {
                            filteredOrderList.add(orderResponse.getOrders().get(i));
                        }
                    }
                } else {
                    activityMainBinding.latestOrderContainer.setVisibility(View.GONE);
                }

                orderList.clear();
                orderList.addAll(filteredOrderList);

                OrderListAdapter adapter = new OrderListAdapter(context, orderList);
                activityMainBinding.orderList.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                activityMainBinding.orderList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                activityMainBinding.orderList.setStackFromBottom(true);
            }
        };
        registerReceiver(broadcastReceiver,filter);

        getSupportActionBar().hide();

        OrderListAdapter adapter = new OrderListAdapter(this, orderList);
        activityMainBinding.orderList.setAdapter(adapter);
    }

    void setupOrderList(ActivityMainBinding activityMainBinding) {
        for (int i = 0; i < 4; i++) {
            orderList.add(new Order("000" + i, "READY"));
        }

        OrderListAdapter adapter = new OrderListAdapter(this, orderList);
        activityMainBinding.orderList.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}
