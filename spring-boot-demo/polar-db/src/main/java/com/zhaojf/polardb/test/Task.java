package com.zhaojf.polardb.test;

import com.zhaojf.polardb.entity.EpcInfo;
import com.zhaojf.polardb.service.EpcInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Task {



    private final EpcInfoService epcInfoService;

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 10000,
            20, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), r -> {
        final Thread thread = new Thread(r);
        thread.setPriority(1);
        return thread;
    });

    public Task(EpcInfoService epcInfoService) {
        this.epcInfoService = epcInfoService;
    }


    @PostConstruct
    public void start() {
        for(int i = 0; i<100; i++){
            threadPoolExecutor.execute(() -> {
                try {
                    this.task();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

    }


    public void task() throws InterruptedException {

        final String str = UUID.randomUUID().toString().toUpperCase();
        final String sku = str.substring(0, 8);
        List<EpcInfo> list = new ArrayList<>();
        for(int i = 0; i<= 500; i++){
            final String epc = UUID.randomUUID().toString().toUpperCase();
            final String style_code = sku.substring(0, 5);
            final String size = sku.substring(5, 6);
            final String color = sku.substring(6, 8);
            final String tid = UUID.randomUUID().toString().toUpperCase();
            final EpcInfo epcInfo = new EpcInfo(epc, tid, sku, style_code, size, color, "");
            list.add(epcInfo);
        }

        epcInfoService.saveBatch(list);
        System.out.println("插入数据成功！");
        threadPoolExecutor.execute(() -> {
            try {
                this.task();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


}
