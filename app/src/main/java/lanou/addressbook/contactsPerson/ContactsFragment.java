package lanou.addressbook.contactsPerson;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import lanou.addressbook.DataBase.DBTools;
import lanou.addressbook.R;

/**
 *
 * Created by dllo on 16/9/20.
 */
public class ContactsFragment extends Fragment{

    private ListView lv_contactPerson;
    private DBTools handle_data;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        handle_data = new DBTools(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contacts_fragment, null);
        lv_contactPerson = (ListView) v.findViewById(R.id.lv_contactsPerson);
        initData();
        ContactPersonAdapter contactadapter = new ContactPersonAdapter(getActivity());
        Log.d("ContactsFragment", "handle_data.querydata():" + handle_data.querydatafromcontactperson());

        contactadapter.setPersonBeen_list(handle_data.querydatafromcontactperson());
        lv_contactPerson.setAdapter(contactadapter);
        return  v;
    }

    private void initData() {
//        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//
//                String person_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                // Log.d("MyHelper", "name" + person_name);
//
//                String person_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                Log.d("MyHelper", "number" + person_number);
//                long phone_id = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
//                String person_ID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
//                Log.d("MainActivity", "personid" + person_ID);
//                Bitmap bitmap;
//                if(phone_id > 0) {
//
//                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(person_ID));
//                    Log.d("MainActivity", "uri:" + uri);
//                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(), uri, false);
//                    Log.d("MyHelper", "input:" + input);
//                    bitmap = BitmapFactory.decodeStream(input);
//                    Log.d("MyHelper", "bitmap:" + bitmap);
//                } else  {
//                    bitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.mipmap.ic_launcher);
//                }
//                ContactPersonBean bean = new ContactPersonBean();
//                String person_number1 = handle_data.transer(person_number);
//                bean.setBitmap(bitmap);
//                bean.setName(person_name);
//                bean.setNumber(person_number1);
//                handle_data.insert_contactperson_data(bean);
//
//            }
//
//        } cursor.close();
    }


}
