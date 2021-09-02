package com.example.androiddemo;
import org.junit.Test;

import org.junit.Assert.*;

import java.util.Queue;

public class ThreadUnitTest {
    private final static int MAX_SIZE=100;
    @Test
    private void WaitUnitTest(){
        Queue<Integer> queue = null;
        //生产线程
        synchronized (queue){
            while(queue.size()==MAX_SIZE){
                try{
                    queue.wait();
                }catch (Exception e){
                    e.printStackTrace();
                }
                queue.add(1);
                queue.notifyAll();
            }
        }

        //消费线程
        synchronized (queue){
            while (queue.size()==0){
                try{
                    queue.wait();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            queue.poll();
            queue.notifyAll();
        }
    }
}
