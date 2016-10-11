package lanou.addressbook.Data;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

import lanou.addressbook.DataBase.DBTools;
import lanou.addressbook.contactList.ContactEneity;

/**
 * Created by dllo on 16/10/10.
 */
public class CallLog_Data implements Runnable {
    private Context context;

    public CallLog_Data(Context context) {
        this.context = context;
    }


    @Override
    public void run() {
        // 存数据
        // 权限检查
        DBTools tools = new DBTools(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // 获取系统的通话记录并插入本地数据库
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));

                ContactEneity eneity = new ContactEneity(name == null ? tools.isMatch(number) : name, number, date);
                tools.insert_data(eneity);
            }
        }
        assert cursor != null;
        cursor.close();
        // 查询本地数据库
    }
}
