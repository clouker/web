package org.clc.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {

    public static void main(String[] args) {
        go();
    }

    public static void go() {
        // 创建一个线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Callable<Map>> list = new ArrayList<>();
//        list.add(new Job());
//        list.add(Web.init("http://sfz.ckd.cc/idcard.php", null, null));
//        list.add(new Job(Web.get("http://sfz.ckd.cc/idcard.php", null, null, null,null)));
        list.add(new Job(Web.get("https://s7.addthis.com/l10n/client.zh.min.json", null, null, null, null)));
        try {
            // 线程池加载线程
            List<Future<Map>> futures = executor.invokeAll(list);
            // 获取每个线程返回的结果，可以进行合并，作为多并发的结果
            for (Future<Map> future : futures)
                System.out.println(((Map) future.get()).get("content"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
