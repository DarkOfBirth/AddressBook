package lanou.addressbook.ChatMessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import lanou.addressbook.DataBase.DBTools;
import lanou.addressbook.R;
import lanou.addressbook.message.MessageBean;

/**
 * Created by dllo on 16/10/8.
 */
public class Chat extends Activity {
    DBTools tools ;
    private MessageBean messageBean;
    private RecyclerView rv_chat;
    private ArrayList<MessageBean> chatlist = new ArrayList<>();
    private TextView name;
    private Button btn_send;
    private EditText et_body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        name = (TextView) findViewById(R.id.name);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_body = (EditText) findViewById(R.id.et_body);

        tools = new DBTools(this);
        Intent intent = getIntent();
        messageBean = new MessageBean();
        messageBean =  (MessageBean) intent.getSerializableExtra("data");
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

            }
        });

    }

    private ArrayList<MessageBean> getData(MessageBean messageBean) {
        String phonenumber = messageBean.getPhonenumber();
        Log.d("Chat","number" + phonenumber);
        return tools.query_message_bynumber(phonenumber);

    }
}
