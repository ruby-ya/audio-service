package com.example.audioservice;

import cn.hutool.extra.ssh.Sftp;
import com.example.audioservice.config.AudioServiceDowloadConfig;
import com.example.audioservice.config.AudioServiceSftpConfig;
import com.example.audioservice.service.SftpSevice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AudioServiceApplicationTests {
    @Autowired
    private AudioServiceDowloadConfig dowloadConfig;

    @Autowired
    private AudioServiceSftpConfig sftpConfig;

    @Autowired
    private SftpSevice sftpSevice;

    @Test
    void contextLoads() {
        Sftp sftp = new Sftp(sftpConfig.getHost(), sftpConfig.getPort(), sftpConfig.getUsername(), sftpConfig.getPassword());

        System.out.println("hello");
    }

    @Test
    void testSftpService() {
        sftpSevice.copyFileToNewServer();
    }
}
