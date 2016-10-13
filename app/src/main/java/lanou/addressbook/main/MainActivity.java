package lanou.addressbook.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Date;

import lanou.addressbook.R;
import lanou.addressbook.contactList.ContactListFragment;
import lanou.addressbook.contactsPerson.ContactsFragment;
import lanou.addressbook.message.MessageFragment;
import lanou.addressbook.phoneDial.DialAdapter;
import lanou.addressbook.phoneDial.DialFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout tb;
    private ViewPager vp;


    private ArrayList<Fragment> datas = new ArrayList<>();
    private MyReceiver cast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 装载数据
        initData();
        // 装载视图
        initView();
        initTab();
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 3) {
                    MessageFragment messageFragment = (MessageFragment) datas.get(3);
                    messageFragment.dissMissPop();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        cast = new MyReceiver();
        // 开始注册
        Log.d("MainActivity", "开始注册");
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(cast, filter);
        Log.d("MainActivity", "注册结束");

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    /**
     * 短信通知
     *
     * @param title
     * @param body
     * @param time
     */
    private void messageNotification(String title, String body, String time) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        Notification.Builder builder = new Notification.Builder(this);
        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this);

        builder1.setTicker("您收到一条新信息");
        builder1.setSmallIcon(R.mipmap.ic_launcher);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.message_notify);

        Log.d("MainActivity1", title + body);
        Intent intent = new Intent(this, MainActivity.class);
        views.setTextViewText(R.id.tv_msg_notify_title, title);
        views.setTextViewText(R.id.tv_msg_notify_body, body);
        views.setTextViewText(R.id.tv_msg_notify_time, time);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 555, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder1.setContentIntent(pendingIntent);
        builder1.setContent(views);


        Notification notification = builder1.build();

        managerCompat.notify(101, notification);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cast);
    }

    private void initTab() {
        int[] selectors = {R.drawable.tab_selector_tel, R.drawable.tab_selector_callrecords
                , R.drawable.tab_selector_contact, R.drawable.tab_selector_sms};
        for (int i = 0; i < selectors.length; i++) {
            tb.getTabAt(i).setIcon(selectors[i]);
        }
    }

    private void initView() {
        tb = (TabLayout) findViewById(R.id.tb);
        vp = (ViewPager) findViewById(R.id.vp);
        DialAdapter adapter = new DialAdapter(getSupportFragmentManager());
        adapter.setList(datas);
        vp.setAdapter(adapter);
        tb.setupWithViewPager(vp);

    }

    private void initData() {
        DialFragment dialFragment = new DialFragment();
        ContactListFragment contactlistfragment = new ContactListFragment();
        contactlistfragment.setContext(this);
        ContactsFragment contactsfragment = new ContactsFragment();
        MessageFragment messagefragment = new MessageFragment();
        datas.add(dialFragment);
        datas.add(contactlistfragment);
        datas.add(contactsfragment);
        datas.add(messagefragment);
    }

    class MyReceiver extends BroadcastReceiver {
        private SmsMessage[] smsMessages;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MyReceiver", "收到广播");
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                abortBroadcast();
                Bundle bundle = intent.getExtras();
                if (bundle != null) {//如果数据不为空
                    //获得收到的短信数据
                    Object[] objArray = (Object[]) bundle.get("pdus");

                    smsMessages = new SmsMessage[objArray.length];
                    StringBuffer sb = new StringBuffer();
                    sb.append("时间：" + new DateFormat().format(" hh.mm", new Date()));
                    String time = new DateFormat().format(" hh:mm", new Date()) + "";
                    String title = "";
                    String body = "";
                    for (int i = 0; i < smsMessages.length; i++) {
                        smsMessages[i] = SmsMessage.createFromPdu((byte[]) objArray[i]);
                        //获取短信发送地址
                        sb.append("发送者：" + smsMessages[i].getOriginatingAddress());
                        title = smsMessages[i].getOriginatingAddress();
                        //获取短信内容
                        sb.append("短信内容：" + smsMessages[i].getDisplayMessageBody() + "\n");
                        body = smsMessages[i].getDisplayMessageBody();

                    }


                    Log.d("MyReceiver", "收到短信");
                    //收到短信
                    messageNotification(title, body, time);

                    // Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

