package lanou.addressbook.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import lanou.addressbook.R;

/**
 * Created by dllo on 16/9/29.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
    private ArrayList<MessageBean> arraylist;


    OnRecyclerItemClick_Message onRecyclerItemClick_message;

    public void setOnRecyclerItemClick_message(OnRecyclerItemClick_Message onRecyclerItemClick_message) {
        this.onRecyclerItemClick_message = onRecyclerItemClick_message;
    }

    private Context context;
    public MessageAdapter(Context context) {
        this.context = context;
    }
    public void setArraylist(ArrayList<MessageBean> arraylist) {
        this.arraylist = arraylist;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(arraylist.get(position).getName());
        holder.content.setText(arraylist.get(position).getContent());
        // 时间戳转为日期
        String date = arraylist.get(position).getDate();
        Log.d("MessageAdapter","时间戳" + date);

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        String time = format.format(Long.parseLong(date));
        holder.date.setText(time);
        holder.ll_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MessageAdapter", "arraylist.get(position):" + arraylist.get(position).getPhonenumber());
                onRecyclerItemClick_message.onItemClick(arraylist.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arraylist == null ? 0 : arraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView content;
        private final TextView date;
        private final LinearLayout ll_message;

        public MyViewHolder(View itemView) {
            super(itemView);
             ll_message =  (LinearLayout)itemView.findViewById(R.id.ll_message);
            name = (TextView) itemView.findViewById(R.id.tv_name_message);
            content = (TextView) itemView.findViewById(R.id.tv_content_messgge);
            date = (TextView) itemView.findViewById(R.id.tv_date_message);



        }
    }
}
