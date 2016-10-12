package lanou.addressbook.message;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.HashMap;

import lanou.addressbook.ChatMessage.Chat;
import lanou.addressbook.Data.Message_Data;
import lanou.addressbook.DataBase.DBTools;
import lanou.addressbook.R;
import lanou.addressbook.guide.SingleSimpleThreadPool;


public class MessageFragment extends Fragment implements OnRecyclerItemClick_Message, View.OnClickListener {

    private static HashMap<String, String> maps;
    private ArrayList<MessageBean> messagebeans;
    private DBTools tools;
    private SQLiteDatabase db;
    private Button add;
    private Context context;
    private PopupWindow popupWindow;
    private Button send_pop;
    private RecyclerView rv_message;
    private MessageAdapter messageAdapter;
    private MessageCustomBroadCast cast;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // 广播的注册........
        cast = new MessageCustomBroadCast();
        IntentFilter filter = new IntentFilter("lanou.addressbook.ChatMessage.message");
        context.registerReceiver(cast, filter);
        return inflater.inflate(R.layout.message_fragment, null);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        messagebeans = new ArrayList<>();
        tools = new DBTools(context);
        rv_message = (RecyclerView) view.findViewById(R.id.rv_message);
        messageAdapter = new MessageAdapter(context);
        init();
        View view_title = view.findViewById(R.id.title_message);

        view_title.setOnClickListener(this);
        add = (Button) view_title.findViewById(R.id.add);
        add.setOnClickListener(this);

        super.onViewCreated(view, savedInstanceState);
    }
    /**
     * popWindow的初始化
     */
    private void initPop() {
        // 显示popwindow 需要三步
        //1. 初始化一个pop
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 2. 给pop添加一个view
        View view = LayoutInflater.from(context).inflate(R.layout.pop, null);
        // 获取到pop上边的textview, 然后点击textview, 让pop消失
        send_pop = (Button) view.findViewById(R.id.send_pop);
        send_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        popupWindow.setContentView(view);
        //3. 显示出来
        // 如果没有2,3 参数的话, 默认popwindow在btn的左下角
        // 参数一: pop显示在哪个view的下边
        // 参数二: 对应x轴的偏移
        // 参数三: 对应y轴的偏移

        popupWindow.showAsDropDown(add, 0, 10);

    }


    private void init() {
        messagebeans = initData();
        // 用来存放电话号码 与 对应的人名,减少数据库的查询
        maps = new HashMap<>();
        for (int i = 0; i < messagebeans.size(); i++) {
            maps.put(messagebeans.get(i).getPhonenumber(), messagebeans.get(i).getName());
        }
        messageAdapter.setArraylist(messagebeans);
        messageAdapter.setOnRecyclerItemClick_message(this);
        rv_message.setAdapter(messageAdapter);
        rv_message.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_message.setLayoutManager(manager);
    }

    /**
     * 发送短信
     */
    private void sendMessage() {
        final EditText number;
        final EditText body;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.send_message_dialog, null);
        number = (EditText) view.findViewById(R.id.number_message_dialog);
        body = (EditText) view.findViewById(R.id.body_message_dialog);
        builder.setView(view);
        builder.setTitle("发送短信");
        builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SmsManager manager = SmsManager.getDefault();
                /**
                 * 第一个参数: 短信的收件人
                 * 第二个参数: 短信服务中心号码, 默认null
                 * 第三个参数: 发送的短信内容
                 * 第三个参数: 发送的短信内容
                 * 第四个参数: 发送成功的一个广播
                 * 第五个参数: 接收成功的一个广播
                 */
                if (number.getText().toString() == "请输入电话号码") {
                    new AlertDialog.Builder(context).setTitle("警告").setMessage("电话号码不能为空").show();

                } else if (body.getText().toString() == "短信内容") {
                    new AlertDialog.Builder(context).setTitle("警告").setMessage("短信内容不能为空").show();
                } else {

                    manager.sendTextMessage(number.getText().toString(), null, body.getText().toString(), null, null);
                    String phonenumber = number.getText().toString();
                    String name = maps.get(phonenumber);
                    String content = body.getText().toString();
                    int type = 2;
                    long time = System.currentTimeMillis();
                    MessageBean message = new MessageBean(name, content, time + "", phonenumber, type);
                    refreshListView(message);
//                    try {
//
//
//                        Thread.sleep(200);
//                        SingleSimpleThreadPool.getInstance().getThreadPool().execute(new Message_Data(context));
//
//                        Thread.sleep(200);
//                        popupWindow.dismiss();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    init();


                }
            }
        });
        builder.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("MessageFragment", "进入后台");
        SingleSimpleThreadPool.getInstance().getThreadPool().execute(new Message_Data(context));
    }

    @Override
    public void onResume() {
        super.onResume();

        //init();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private ArrayList<MessageBean> initData() {
        Log.d("MessageFragment", "初始化数据");
        // 这个可以
        ArrayList<MessageBean> beans;
//        Cursor cursor = getActivity().getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, null, null);
//        Log.d("MessageFragment", "cursor.getCount():" + cursor.getCount());
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                String date = cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE));
//
//                String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
//                String name = tools.isMatch(tools.transer(address));
//                int type = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE));
//
//                String address2 = tools.transer(address);
//                Log.d("MessageFragment", "电话" + address2);
//                String content = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
//                MessageBean bean = new MessageBean();
//                bean.setPhonenumber(address2 == null ? "0" : address2);
//                bean.setName(name == null ? "空" : name);
//                bean.setDate(date == null ? "0" : date);
//                bean.setContent(content == null ? "空" : content);
//                bean.setType(type == 0 ? 0 : type);
//                tools.insert_message_data(bean);
//            }
//        }
//        cursor.close();

        // 获取数据

        // beans = tools.query_message_data();
        beans = tools.query_message_data_simple();
        return beans;
    }


    @Override
    public void onItemClick(MessageBean chatbean) {
        Intent intent = new Intent(getActivity(), Chat.class);
        Log.d("MessageFragment1", "chatbean:" + chatbean.getPhonenumber());
        intent.putExtra("data", chatbean);
        startActivity(intent);
    }

    private void refreshListView(MessageBean message) {

        for (int i = 0; i < messagebeans.size(); i++) {
            Log.d("MessageFragment", messagebeans.get(i).getPhonenumber());

            if (message.getPhonenumber().equals(messagebeans.get(i).getPhonenumber())) {
                Log.d("MessageFragment", "查到");
                messagebeans.add(0, message);
                messagebeans.remove(i + 1);
                messageAdapter.setArraylist(messagebeans);
                return;
            }
        }

        messagebeans.add(0, message);
        messageAdapter.setArraylist(messagebeans);
        Log.d("MessageFragment", "跳出循环");
    }
    public void dissMissPop() {
        if (popupWindow != null) {

            popupWindow.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_message:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                break;
            case R.id.add:
                if(popupWindow != null && popupWindow.isShowing() ) {
                    popupWindow.dismiss();
                }
                else {
                    initPop();
                }
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(cast);
    }

    class MessageCustomBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 参数intent, 就是无序广播携带值的 intent
            MessageBean bean = (MessageBean) intent.getSerializableExtra("messagedata");
            Log.d("MessageCustomBroadCast", "bean:" + bean);
            Log.d("MessageCustomBroadCast", bean.getPhonenumber());
            bean.setName(maps.get(bean.getPhonenumber()));
            Log.d("MessageCustomBroadCast", bean.getName());
            Log.d("MessageCustomBroadCast", bean.getContent());
            refreshListView(bean);


        }
    }
}
