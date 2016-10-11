package lanou.addressbook.main;

import android.app.Application;
import android.content.Context;

/**
 * Created by dllo on 16/9/26.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
    /**
     * 获取全局上下文*/
    public static Context getContext() {
        return context;
    }
}
