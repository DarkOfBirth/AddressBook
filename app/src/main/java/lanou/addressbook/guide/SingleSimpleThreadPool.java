package lanou.addressbook.guide;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by dllo on 16/10/10.
 */
public class SingleSimpleThreadPool {

    private static SingleSimpleThreadPool singleSimpleThreadPool;
    private static  ThreadPoolExecutor threadPool ;

    /**
     * 构造方法
     */
    private SingleSimpleThreadPool() {
        int cpuCore = Runtime.getRuntime().availableProcessors();
        threadPool = new ThreadPoolExecutor(cpuCore + 1, cpuCore * 2 + 1, 30L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>());

    }

    public static SingleSimpleThreadPool getInstance() {
        if (singleSimpleThreadPool == null) {
            synchronized (SingleSimpleThreadPool.class) {
                if (singleSimpleThreadPool == null) {

                    singleSimpleThreadPool = new SingleSimpleThreadPool();
                }
            }
        }
        return singleSimpleThreadPool;
    }

    public  ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }
}
