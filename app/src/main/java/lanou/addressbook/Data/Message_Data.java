package lanou.addressbook.Data;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.Telephony;
import android.util.Log;

import lanou.addressbook.DataBase.DBTools;
import lanou.addressbook.message.MessageBean;

/**
 * Created by dllo on 16/10/10.
 */
public class Message_Data implements Runnable {
    private Context context;

    public Message_Data(Context context) {
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        DBTools tools = new DBTools(context);
        Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, null, null);
        Log.d("MessageFragment", "cursor.getCount():" + cursor.getCount());
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE));

                String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
                String name = tools.isMatch(tools.transer(address));
                int type = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE));

                String address2 = tools.transer(address);
                Log.d("MessageFragment", "电话" + address2);
                String content = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                MessageBean bean = new MessageBean();
                bean.setPhonenumber(address2 == null ? "0" : address2);
                bean.setName(name == null ? "空" : name);
                bean.setDate(date == null ? "0" : date);
                bean.setContent(content == null ? "空" : content);
                bean.setType(type == 0 ? 0 : type);
                tools.insert_message_data(bean);
            }
        }
        cursor.close();
    }
}
