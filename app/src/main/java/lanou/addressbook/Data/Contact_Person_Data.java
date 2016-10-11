package lanou.addressbook.Data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.InputStream;

import lanou.addressbook.DataBase.DBTools;
import lanou.addressbook.R;
import lanou.addressbook.contactsPerson.ContactPersonBean;
import lanou.addressbook.main.MyApplication;

/**
 * Created by dllo on 16/10/10.
 */
public class Contact_Person_Data implements Runnable{
    private Context context;

    public Contact_Person_Data(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                DBTools tools = new DBTools(context);
                String person_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                // Log.d("MyHelper", "name" + person_name);

                String person_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d("MyHelper", "number" + person_number);
                long phone_id = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
                String person_ID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                Log.d("MainActivity", "personid" + person_ID);
                Bitmap bitmap;
                if(phone_id > 0) {

                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(person_ID));
                    Log.d("MainActivity", "uri:" + uri);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri, false);
                    Log.d("MyHelper", "input:" + input);
                    bitmap = BitmapFactory.decodeStream(input);
                    Log.d("MyHelper", "bitmap:" + bitmap);
                } else  {
                    bitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.mipmap.ic_launcher);
                }
                ContactPersonBean bean = new ContactPersonBean();
                String person_number1 = tools.transer(person_number);
                bean.setBitmap(bitmap);
                bean.setName(person_name);
                bean.setNumber(person_number1);
                tools.insert_contactperson_data(bean);

            }

        } cursor.close();
    }
}
