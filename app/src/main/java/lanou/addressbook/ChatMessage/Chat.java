package lanou.addressbook.ChatMessage;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import lanou.addressbook.Data.Message_Data;
import lanou.addressbook.DataBase.DBTools;
import lanou.addressbook.R;
import lanou.addressbook.guide.SingleSimpleThreadPool;
import lanou.addressbook.message.MessageBean;

/**
 * Created by dllo on 16/10/8.
 */
public class Chat extends Activity implements View.OnClickListener {
    DBTools tools;
    private MessageBean messageBean;
    private RecyclerView rv_chat;
    private ArrayList<MessageBean> chatlist = new ArrayList<>();
    private TextView name;
    private Button btn_send;
    private EditText et_body;
    private TextView chat_add;
    private PopupWindow popupWindow;
    private Button send_delay;
    private View view_delay_chat;
    private String delay_time;
    private EditText et_chat_delay_time;
    private DelayMessageService.MyBinder binder;
    private AlertDialog.Builder builder;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        SingleSimpleThreadPool.getInstance().getThreadPool().execute(new Message_Data(this));

        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        name = (TextView) findViewById(R.id.name);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_body = (EditText) findViewById(R.id.et_body);
        chat_add = (TextView) findViewById(R.id.Chat_add);


        tools = new DBTools(this);
        Intent intent = getIntent();
        messageBean = new MessageBean();
        messageBean = (MessageBean) intent.getSerializableExtra("data");
        Log.d("Chat", "bean:number" + messageBean.getPhonenumber());
        name.setText(messageBean.getName() == null ? messageBean.getPhonenumber() : messageBean.getName());
        final ChatAdapter adapter = new ChatAdapter(this);

        rv_chat.setAdapter(adapter);
        chatlist = getData(messageBean);
        adapter.setMessageBeen(chatlist);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_chat.setLayoutManager(manager);


        // 短信内容界面的发送
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message_body = et_body.getText().toString();
                adapter.sendMessage(message_body);
                Log.d("Chat", "为什么不清空");
                et_body.setText("");
                et_body.setHint("请输入内容");
            }
        });

        chat_add.setOnClickListener(this);
        Intent delay_message_intent = new Intent(this, DelayMessageService.class);
        startService(delay_message_intent);
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (DelayMessageService.MyBinder) service;
                Log.d("Chat", "binder:" + binder);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(delay_message_intent, serviceConnection, BIND_AUTO_CREATE);


    }

    private ArrayList<MessageBean> getData(MessageBean messageBean) {
        String phonenumber = messageBean.getPhonenumber();
        Log.d("Chat", "number" + phonenumber);
        return tools.query_message_bynumber(phonenumber);

    }

    @Override
    protected void onPause() {
        Log.d("Chat", "onPause");
        super.onPause();
        SharedPreferences sp = getSharedPreferences("TemporyMessage", MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        if (et_body.getText().toString() != "请输入内容") {
            // Log.d("Chat", "进入");
            et.putString(messageBean.getPhonenumber(), et_body.getText().toString());
        } else {
            et.putString(messageBean.getPhonenumber(), "请输入内容");

        }

        et.commit();


    }

    private void initPop() {
        // 显示popwindow 需要三步
        //1. 初始化一个pop
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 2. 给pop添加一个view
        view = LayoutInflater.from(this).inflate(R.layout.chat_pop, null, false);
        send_delay = (Button) view.findViewById(R.id.send_delay);
        send_delay.setOnClickListener(this);
        popupWindow.setContentView(view);
        //3. 显示出来
        // 如果没有2,3 参数的话, 默认popwindow在btn的左下角
        // 参数一: pop显示在哪个view的下边
        // 参数二: 对应x轴的偏移
        // 参数三: 对应y轴的偏移


        popupWindow.showAsDropDown(chat_add, 0, 10);
        // 获取到pop上边的textview, 然后点击textview, 让pop消失

    }

    private void send() {
        builder = new AlertDialog.Builder(Chat.this);
        builder.setTitle("延时短信");
        view_delay_chat = LayoutInflater.from(Chat.this).inflate(R.layout.chat_delay, null, false);
        et_chat_delay_time = (EditText) view_delay_chat.findViewById(R.id.et_chat_delay_time);
        builder.setView(view_delay_chat);

        builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                delay_time = et_chat_delay_time.getText().toString();
                Log.d("Chat", delay_time);
                Log.d("Chat", "binder:" + binder);
                final String message_body = et_body.getText().toString();
                MessageBean bean = new MessageBean(chatlist.get(0).getName(), message_body, System.currentTimeMillis() + "", chatlist.get(0).getPhonenumber(), 2);
                //chatlist.add(bean);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        binder.sendMessage(delay_time, message_body, chatlist.get(0).getPhonenumber());
                    }
                }).start();

                et_body.setText("");
            }
        });

        builder.show();

        popupWindow.dismiss();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Chat", "onResume");
        SharedPreferences sp = getSharedPreferences("TemporyMessage", MODE_PRIVATE);
        String str = sp.getString(messageBean.getPhonenumber(), "");
        if (str == "请输入内容") {
            et_body.setHint(str);
        } else {

            et_body.setText(sp.getString(messageBean.getPhonenumber(), ""));
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 短信详情界面的add
            case R.id.Chat_add:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    initPop();
                }

                break;
            case R.id.send_delay:
                send();
                break;
        }
    }
}
