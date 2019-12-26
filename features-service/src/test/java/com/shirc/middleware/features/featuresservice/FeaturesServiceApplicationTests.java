package com.shirc.middleware.features.featuresservice;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.log4j.helpers.ThreadLocalMap;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class FeaturesServiceApplicationTests {

    @Test
    void contextLoads() throws InterruptedException {
        Object obj = new Object();
        ReferenceQueue<Object> refQueue = new ReferenceQueue<Object>();
        //SoftReference<Object> softRef= new SoftReference<Object>(obj,refQueue);
        WeakReference<Object> softRef= new WeakReference<Object>(obj,refQueue);
        System.out.println(softRef.get());// java.lang.Object@f9f9d8  
        System.out.println(refQueue.poll());// null  
        // 清除强引用,触发GC  
        obj = null;
        System.gc();
        System.out.println(softRef.get());
        Thread.sleep(200);
        System.out.println(refQueue.poll());

    }



    @Test
    void test1(){
        ThreadLocal threadLocal = new ThreadLocal();
        for (int i=0;i<10;i++){
            final int v = i;
            Thread thread = new Thread(()->{
                threadLocal.set("threadLocal="+v);
                System.out.println(Thread.currentThread().getName()+"设置-threadLocal="+v);

                Thread.yield();
                Thread.yield();
                Thread.yield();
                System.out.println(Thread.currentThread().getName()+"获取-"+threadLocal.get());

            });
            thread.setName("线程 "+i);
            thread.setDaemon(true);
            thread.start();
        }
        System.out.println(threadLocal.get());
    }


    @Test
    void test2(){
        ThreadLocal threadLocal = new ThreadLocal();
        ThreadLocal threadLocal2 = new ThreadLocal();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                2,
                30,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().setDaemon(true).setNameFormat("线程"+"-%d").build(),
                //队列满了 丢掉任务不抛出异常;
                new ThreadPoolExecutor.DiscardPolicy()
        );
        for (int i=0;i<10;i++){
            final int v = i;
            executor.submit(()->{
                System.out.println(Thread.currentThread().getName()+"获取-"+threadLocal.get());
                Thread.yield();
                Thread.yield();
                Thread.yield();
                threadLocal.set("threadLocal="+v);
                threadLocal2.set("threadLocal2="+v);
                System.out.println(Thread.currentThread().getName()+"设置-threadLocal="+v);
                Thread t = Thread.currentThread();
            });
        }
        executor.getThreadFactory().newThread(()->{
            System.out.println(Thread.currentThread().getName());
        });

    }


    @Test
    void testInheritableThreadLocal(){

     /*   ThreadLocal threadLocal = new ThreadLocal();
        threadLocal.set("父类");
        new Thread(()->{
            System.out.println(threadLocal.get());
        }).start();
        Method getMap = null;
        try {
            getMap = threadLocal.getClass().getDeclaredMethod("getMap", Thread.class);
            getMap.setAccessible(true);
            Object map = getMap.invoke(threadLocal,Thread.currentThread());

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
*/

        InheritableThreadLocal inheritableThreadLocal = new InheritableThreadLocal();
        inheritableThreadLocal.set("父类");
        new Thread(()->{
           System.out.println(inheritableThreadLocal.get());
        }).start();
    }


    @Test
    void testThreadLocalWeakReference(){
        ThreadLocal threadLocal = new ThreadLocal();
        Object a = new Object();
        threadLocal.set(a);
        System.out.println(threadLocal.get());
        a = null;
        System.out.println(threadLocal.get());
        System.gc();
        System.out.println(threadLocal.get());
    }
}
