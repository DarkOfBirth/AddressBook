package lanou.addressbook.ChatMessage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import lanou.addressbook.R;
import lanou.addressbook.message.MessageBean;

/**
 * Created by dllo on 16/10/8.
 */
public class ChatAdapter extends RecyclerView.Adapter {
    ArrayList<MessageBean> messageBeen;
    Context context;

    public ChatAdapter(Context context) {

        this.context = context;
    }

    public void setMessageBeen(ArrayList<MessageBean> messageBeen) {
        this.messageBeen = messageBeen;
    }

    @Override
    public int getItemViewType(int position) {
        return messageBeen.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            // 1 接收
            case 1:
            {
                View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
                LeftViewHolder leftViewHolder = new LeftViewHolder(view);
                return leftViewHolder;
            }
            // 2 发送
            case 2:
            {
                View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
                RightViewHolder rightViewHolder = new RightViewHolder(view);
                return rightViewHolder;

            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            // 1 接收
            case 1:

                LeftViewHolder leftViewHolder = (LeftViewHolder) holder;
                leftViewHolder.tv_left.setText(messageBeen.get(position).getContent());
                break;
            case 2:

                RightViewHolder rightViewHolder= (RightViewHolder) holder;
                rightViewHolder.tv_right.setText(messageBeen.get(position).getContent());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageBeen == null ? 0 : messageBeen.size();
    }

    /**
     * 短信发送跳转
     * @param message_body
     */
    public void sendMessage(String message_body) {
        SmsManager manager= SmsManager.getDefault();

        if(message_body != null) {
            Log.d("ChatAdapter", messageBeen.get(0).getPhonenumber());
            Log.d("ChatAdapter", message_body);
        manager.sendTextMessage(messageBeen.get(0).getPhonenumber(),null,message_body,null,null);
            messageBeen.add(new MessageBean(null,message_body,null,null,2));
            notifyDataSetChanged();


        }

    }

    private class LeftViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_left;

        public LeftViewHolder(View view) {
            super(view);
            tv_left = (TextView) view.findViewById(R.id.tv_chat_left);
        }
    }

    private class RightViewHolder extends  RecyclerView.ViewHolder{

        private final TextView tv_right;

        public RightViewHolder(View view)  {
            super(view);
            tv_right = (TextView)view.findViewById(R.id.tv_chat_right);
        }
    }
}
