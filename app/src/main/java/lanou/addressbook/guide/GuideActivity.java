package lanou.addressbook.guide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;

import lanou.addressbook.Data.CallLog_Data;
import lanou.addressbook.Data.Contact_Person_Data;
import lanou.addressbook.R;
import lanou.addressbook.main.MainActivity;

/**
 * Created by dllo on 16/9/22.
 */
public class GuideActivity extends Activity {
    Intent  intent = null;
    private TextView guide_tv;
    private boolean actived = true;
    private ImageView guide_iv;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        context = GuideActivity.this;

        guide_tv = (TextView) findViewById(R.id.tv_guide);
        guide_iv = (ImageView) findViewById(R.id.iv_guide);
        intent = new Intent(GuideActivity.this, MainActivity.class);
        // 获取引导页的图片
        getGuidePicture();


        // 倒计时初始化及启动
        Counter counter = new Counter(5000,1000);
        counter.start();

        /**
         * 计时器点击事件,实现跳转
         */
        guide_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                actived = false;
                GuideActivity.this.finish();

            }
        });




        // 线程池
        executeThreadPool();
    }

    /**
     * 线程池操作
     */
    private void executeThreadPool() {
        SingleSimpleThreadPool singleSimpleThreadPool = SingleSimpleThreadPool.getInstance();
        ThreadPoolExecutor executor = singleSimpleThreadPool.getThreadPool();

        executor.execute(new CallLog_Data(context));
        executor.execute(new Contact_Person_Data(context));

    }

    /**
     * 获取图片
     */
    public void getGuidePicture() {
        // 异步类获取图片
        ImageAsyncTask imageAsyncTask = new ImageAsyncTask();
        String strurl = "http://www.81835.com/UploadFile/2015-11/20151112042772498.jpg";
        imageAsyncTask.execute(strurl);
    }


    class Counter extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            guide_tv.setText("跳过 " + millisUntilFinished/1000  );
        }

        @Override
        public void onFinish() {
            if(actived ) {

                startActivity(intent);
                GuideActivity.this.finish();
            }

        }
    }

    // 三个泛型参数: 1.Params: 决定的是doInBackground方法里参数的类型, 和开启异步任务的参数类型
    //                 2.progress: 决定了 onProgressUpdate 方法参数的类型
    //                  3.result   决定   doInBackground方法的返回值得类型 和 postExecute() 方法的参数类型
    class ImageAsyncTask extends AsyncTask<String, Object,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    connection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            guide_iv.setImageBitmap(bitmap);
        }
    }
}
