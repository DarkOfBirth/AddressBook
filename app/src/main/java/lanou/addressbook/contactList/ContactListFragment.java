package lanou.addressbook.contactList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import lanou.addressbook.DataBase.DBTools;
import lanou.addressbook.R;
// 联系人

/**
 * Created by dllo on 16/9/19.
 */
public class ContactListFragment extends Fragment {

    private ListView contactList;
    private Context context;
    private Button delete;
    private DBTools handle_data;
    private ArrayList<ContactEneity> eneities = new ArrayList<>();

    public ContactListFragment() {
        super();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        handle_data = new DBTools(getActivity());

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.contactlist_fragment, null);

        contactList = (ListView) v.findViewById(R.id.contactlist_litview);
        delete = (Button) v.findViewById(R.id.btn_delete);
        final ContactListAdapter contactListAdapter = new ContactListAdapter(getActivity());
        // 添加数据
        initData();
        contactListAdapter.setDelete_Button(delete);
        contactListAdapter.setEneities(eneities);
        contactListAdapter.setListView(contactList);
        contactList.setAdapter(contactListAdapter);
        // 打电话
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = "tel:" + eneities.get(position).getNumber().toString();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(str));
                startActivity(intent);

            }
        });

        // 删除事件
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactListAdapter.delete();
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 添加数据, 从数据库获取数据
     */
    private void initData() {
        // 查询本地数据库
        eneities = handle_data.querydata();
        Log.d("ContactListFragment", "eneities:" + eneities);

    }

    /**
     * 未使用到的方法
     * 根据电话号码匹配姓名
     *
     * @param number 电话号码
     */
    //    private String matchName(String number) {
    //        Cursor cursor = getActivity().getContentResolver().query(Uri.withAppendedPath(
    //                ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number), new String[]{
    //                ContactsContract.PhoneLookup._ID,
    //                ContactsContract.PhoneLookup.NUMBER,
    //                ContactsContract.PhoneLookup.DISPLAY_NAME,
    //                ContactsContract.PhoneLookup.TYPE, ContactsContract.PhoneLookup.LABEL}, null, null, null);
    //        if (cursor.getCount() == 0) {
    //            //没找到电话号码
    //            return null;
    //        } else if (cursor.getCount() > 0) {
    //            cursor.moveToFirst();
    //            String phonename = cursor.getString(2); //获取姓名
    //            Log.d("MainActivity", phonename);
    //            return phonename;
    //
    //        } else {
    //            return null;
    //        }
    //
    //    }
}
