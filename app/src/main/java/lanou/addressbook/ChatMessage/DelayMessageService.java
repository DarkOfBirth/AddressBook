package lanou.addressbook.ChatMessage;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by dllo on 16/10/17.
 */
public class DelayMessageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MyBinder binder = new MyBinder();
        Log.d("DelayMessageService", "onBind");
        return binder;
    }

    class MyBinder extends Binder {
        public void sendMessage(String delayTime, String messageBody, String messageNumber) {
            Log.d("MyBinder", messageBody);
            try {
                Thread.sleep(Integer.parseInt(delayTime) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SmsManager manager = SmsManager.getDefault();

            manager.sendTextMessage(messageNumber, null, messageBody, null, null);

            //            MessageBean bean = new MessageBean(messageBeen.get(0).getName(), message_body, System.currentTimeMillis() + "", phonenumber_message, 2);
            //            messageBeen.add(bean);
            //            notifyDataSetChanged();
            //            Intent intent = new Intent("lanou.addressbook.ChatMessage.message");
            //
            //            intent.putExtra("messagedata", bean);
            //
            //            context.sendBroadcast(intent);
            Log.d("MyBinder", "发送成功");
        }

    }
}
