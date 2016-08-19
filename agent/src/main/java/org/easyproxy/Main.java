package org.easyproxy;/**
 * Description : 
 * Created by YangZH on 16-8-18
 *  下午1:15
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Description :
 * Created by YangZH on 16-8-18
 * 下午1:15
 */

public class Main {

    public static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);

    public static void main(String[] args) {
//        ExecutorService pool = Executors.newCachedThreadPool();
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("g");
        list.add("h");
        list.add("i");
        list.add("j");
        new Thread(new PutTask(list)).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new TakeTask()).start();
        System.out.println("结束");
    }
}

class TakeTask implements Runnable{

    @Override
    public void run() {
        try {
            int size = Main.queue.size();
            for (int i=0;i<size;i++){
                String data = Main.queue.take();
                System.out.println("data-->"+data);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class PutTask implements Runnable{

    private List<String> list;

    public PutTask(List<String> list){
        this.list = list;
    }

    @Override
    public void run() {
        for (String s:list){
            try {
                System.out.println("生产:" + s);
                Main.queue.put(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("生产结束,"+Main.queue.size());
    }
}