package com.yl.distribute.scheduler.client.test;

import java.util.Map;
import java.util.Map.Entry;
import com.yl.distribute.scheduler.client.JobClient;
import com.yl.distribute.scheduler.client.resource.ResourceManager;
import com.yl.distribute.scheduler.common.bean.HostInfo;
import com.yl.distribute.scheduler.common.bean.JobRequest;

class ProduceThread extends Thread{
    
    private JobClient client;
    private int index;
    
    public ProduceThread(JobClient client,int index) {
        this.client = client;
        this.index = index;
    }
    
    public void run() {
        JobRequest input = new JobRequest();
        input.setRequestId(String.valueOf(Math.random() + index));
        input.setCommand("ls -ltr");
        input.setPoolName("pool1");            
        client.submit(input);
    }
}
public class ClientTest {
    
    public static void main(String[] args) throws Exception {
        ResourceManager.getInstance().init();
        JobClient client = JobClient.getInstance();
        for(int i = 0;i < 1; i++) {
            new ProduceThread(client,i).start();
        }
        Thread.sleep(10000);
        Map<String,HostInfo> map = ResourceManager.getInstance().resourceMap;
        for(Entry<String,HostInfo> entry : map.entrySet()) {
            System.out.println("host is " + entry.getKey() +  ",availiable cores is " + entry.getValue().getCores() 
                    +  ",availiable memory is " + entry.getValue().getMemory()); 
        }        
    }
}