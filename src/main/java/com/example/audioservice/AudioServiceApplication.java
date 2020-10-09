package com.example.audioservice;

import cn.hutool.extra.ssh.Sftp;
import com.example.audioservice.config.AudioServiceDowloadConfig;
import com.example.audioservice.config.AudioServiceSftpConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AudioServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AudioServiceApplication.class, args);
    }


    @Autowired
    private AudioServiceDowloadConfig dowloadConfig;
    @Autowired
    private AudioServiceSftpConfig sftpConfig;

    @Bean
    public CommandLineRunner getCommanderRunner() {

        return (String[] args) -> {
            Sftp sftp = new Sftp(sftpConfig.getHost(), sftpConfig.getPort(), sftpConfig.getUsername(), sftpConfig.getPassword());
            //2.判断录音服务器的指定目录是否存在
            String path = sftpConfig.getPath();
            if (path.endsWith("/") || path.endsWith("\\")) {
                path = path.substring(0, path.length() - 1);
            }
            if (!sftp.exist(path)) {
                throw new RuntimeException(String.format("目标服务器[%s]的目录[%s]不存在，应用已停止", sftpConfig.getHost(), sftpConfig.getPath()));
            }
            sftp.close();
        };
    }
}
