package lanou.addressbook.contactList;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import lanou.addressbook.DataBase.DBTools;
import lanou.addressbook.R;


public class ContactListAdapter extends BaseAdapter {
    private boolean visiable = false;
    private ListView listView;
    private ArrayList<ContactEneity> eneities;
    private Context context;
    private DBTools handle_data;
    private String date;

    public void setListView(ListView listView) {
        this.listView = listView;
    }


    public void setDelete_Button(Button delete_Button) {
        this.delete_Button = delete_Button;
    }

    private Button delete_Button;

    public void setEneities(ArrayList<ContactEneity> eneities) {
        this.eneities = eneities;
    }

    public ContactListAdapter(Context context) {
        this.context = context;
        handle_data = new DBTools(context);

    }

    @Override
    public int getCount() {
        return eneities.size();
    }

    @Override
    public Object getItem(int position) {
        return eneities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 删除
    public void  delete() {
        //         正序删除
        //        for (int i = 0, len = eneities.size(); i < len; i++) {
        //            if (eneities.get(i).ischecked() == true) {
        //                eneities.remove(i--);
        //                len--;
        //                Log.d("ContactListAdapter", "len:" + len);
        //            }
        //        }
        // 倒序删除
        for (int i = eneities.size() - 1; i >= 0; i--) {
            if (eneities.get(i).ischecked() == true) {


                handle_data.delete_data(eneities.get(i));
                Log.d("ContactListAdapter", "删除本地");
                // 删除系统数据库
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                String time=eneities.get(i).getDate();
                Log.d("ContactListAdapter","time" + time);


                 int deleteCount = context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.DATE + " = ?", new String[]{time + ""});
                    Log.d("ContactListAdapter", "deleteCount:" + deleteCount);


                eneities.remove(i);

            }
        }
        for (int i = 0; i < eneities.size(); i++) {
            eneities.get(i).setVisibile(false);
            eneities.get(i).setIschecked(false);
        }
        delete_Button.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contactlist, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkBox.setChecked(eneities.get(position).ischecked());
        // checkbox 点击
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 参数v就是代表点击的这个checkbox
                CheckBox checkBox = (CheckBox) v;
                // 获取到了被点击的checkbox点击状态
                Boolean isChecked = checkBox.isChecked();

                // 改变类中的属性 ischecked 的值
                eneities.get(position).setIschecked(isChecked);
            }
        });


        // 长按item
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                visiable = !visiable;


                if (visiable) {
                    for (int i = 0; i < eneities.size(); i++) {
                        eneities.get(i).setVisibile(true);
                        delete_Button.setVisibility(View.VISIBLE);
                    }
                } else {
                    for (int i = 0; i < eneities.size(); i++) {
                        eneities.get(i).setVisibile(false);
                        delete_Button.setVisibility(View.GONE);
                    }
                }


                notifyDataSetChanged();

                return true;
            }
        });

        viewHolder.checkBox.setVisibility(eneities.get(position).isVisibile() ? View.VISIBLE : View.INVISIBLE);
        viewHolder.phonenumber.setText(eneities.get(position).getNumber());
        viewHolder.name.setText(eneities.get(position).getName());
        date = eneities.get(position).getDate();
        Log.d("ContactListAdapter", date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String time = format.format(Long.valueOf(date));
        viewHolder.date.setText(time);

        return convertView;
    }



    class ViewHolder {
        public TextView phonenumber;
        public TextView name;
        public TextView date;
        public CheckBox checkBox;

        public ViewHolder(View convertView) {
            phonenumber = (TextView) convertView.findViewById(R.id.phonenumber_tv);
            name = (TextView) convertView.findViewById(R.id.name_tv);
            date = (TextView) convertView.findViewById(R.id.date_tv);
            checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_ck);
        }
    }
}
