package com.example.audioservice.job;

import com.example.audioservice.service.SftpSevice;
import com.example.audioservice.util.TaskHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Ruby
 * @create 2020-10-02 12:53:20
 */

@Slf4j
@Async
@Component
public class AudioFileSyncJob {
    @Autowired
    private SftpSevice sftpSevice;


    //默认每5分钟执行一次
    @Scheduled(cron = "${audio.cron}")
    //@Scheduled(cron = "0/5 * * * * ?")
    public void runJob() {
        if (TaskHolder.isIsRun()) {
            log.info("存在手动执行扫描任务，当前定时任务取消!");
            return;
        }

        TaskHolder.setIsRun(true);
        log.info("开始  扫描并转移录音文件");

        //文件复制核心业务
        sftpSevice.copyFileToNewServer();

        TaskHolder.setIsRun(false);
        log.info("结束  扫描并转移录音文件");
    }
}
