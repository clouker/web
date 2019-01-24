package org.clc.utils;

import org.clc.utils.http.Web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池
 * List<Callable<Object>> list = new ArrayList<>()
 * list.add(xxx);
 * ThreadPool.run(list)
 * @Author clc
 */
public class ThreadPool {

    public static void main(String[] args) {
        List<Callable<Object>> list = new ArrayList<>();
        list.add(Web.runGet("https://freegeoip.net/json/?callback=jQuery2130860289376552718_1525418233582&_=1525418233583", null, null, null, null));
        list.add(Web.runGet("https://s7.addthis.com/l10n/client.zh.min.json", null, null, null, null));
        run(list);
    }

    public static List<Future<Object>> run(List<Callable<Object>> list) {
        List<Future<Object>> futures = null;
        // 创建一个线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            // 线程池加载线程
            futures = executor.invokeAll(list);
            // 获取每个线程返回的结果，可以进行合并，作为多并发的结果
            for (Future<Object> future : futures)
                System.out.println(LocalDateTime.now() + " : " + future.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        return futures;
    }
}
