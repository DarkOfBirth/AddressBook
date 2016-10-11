package lanou.addressbook.contactsPerson;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lanou.addressbook.R;


/**
 * Created by dllo on 16/9/27.
 */
public class ContactPersonAdapter extends BaseAdapter {
    private ArrayList<ContactPersonBean> personBeen_list;
    private Context context;

    public ContactPersonAdapter(Context context) {
        this.context = context;
    }

    public void setPersonBeen_list(ArrayList<ContactPersonBean> personBeen_list) {
        this.personBeen_list = personBeen_list;
    }

    @Override
    public int getCount() {
        return personBeen_list.size();
    }

    @Override
    public Object getItem(int position) {
        return personBeen_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contact_person,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("ContactPersonAdapter", "viewHolder:" + viewHolder);
        Log.d("ContactPersonAdapter", "getname" + personBeen_list.get(position).getName());
        Log.d("ContactPersonAdapter", "viewHolder.name:" + viewHolder.name);

        viewHolder.name.setText(personBeen_list.get(position).getName());
        viewHolder.number.setText(personBeen_list.get(position).getNumber());
        viewHolder.head_iamge.setImageBitmap(personBeen_list.get(position).getBitmap());


        return convertView;
    }
    class ViewHolder {
        public TextView name;
        public TextView number;
        public MyCircleImagView head_iamge;

        public ViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.contact_name_tv);
            number = (TextView) convertView.findViewById(R.id.contact_number_tv);
            head_iamge = (MyCircleImagView) convertView.findViewById(R.id.head_iv);
        }
    }
}
