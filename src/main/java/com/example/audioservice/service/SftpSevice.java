package com.example.audioservice.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.extra.ssh.Sftp;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.audioservice.config.AudioServiceDowloadConfig;
import com.example.audioservice.config.AudioServiceSftpConfig;
import com.example.audioservice.mapper.AudioFileMapper;
import com.example.audioservice.model.AudioFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Ruby
 * @create 2020-10-02 10:22:22
 */
@Slf4j
@Service
public class SftpSevice {
    @Autowired
    private AudioServiceDowloadConfig dowloadConfig;

    @Autowired
    private AudioServiceSftpConfig sftpConfig;

    @Autowired
    private AudioFileMapper audioFileMapper;

    private String rootPath;

    //拷贝文件
    public void copyFileToNewServer() {
        //1.创建sftp对象
        Sftp sftp = new Sftp(sftpConfig.getHost(), sftpConfig.getPort(), sftpConfig.getUsername(), sftpConfig.getPassword());
        //2.判断录音服务器的指定目录、当前服务器的仓储目录是否存在
        String path = sftpConfig.getPath();
        if (path.endsWith("/") || path.endsWith("\\")) {
            path = path.substring(0, path.length() - 1);
        }
        rootPath = path;
        if (!sftp.exist(path)) {
            log.error("警告！目标服务器[{}]目录[{}]已不存在，文件转移服务无法继续！", sftpConfig.getHost(), path);
        }
        File targetPath = new File(dowloadConfig.getPath());

        if (!targetPath.exists()) {
            //如果目标根目录不存在，就手动创建
            targetPath.mkdirs();
        }

        copyRecursive(sftp, path);

        //关闭sftp，本次复制操作完成
        sftp.close();
    }

    private void copyRecursive(Sftp sftp, String path) {
        //进入当前目录
        boolean cd = sftp.cd(path);
        String pwd = sftp.pwd();
        //列出当前目录中的文件
        List<String> files = sftp.lsFiles(pwd);
        //开始复制文件
        files.forEach(file -> {
            copyFile(sftp, pwd, file);
        });
        if (CollectionUtil.isEmpty(files)) {
            log.info("目录[{}]下为空,等待下次扫描", pwd);
        } else {
            log.info("目录[{}]下文件复制完成", pwd);
        }

        //递归复制
        List<String> dirs = sftp.lsDirs(pwd);
        dirs.forEach(pathE -> copyRecursive(sftp, pathE));

        //复制完成删除当前目录,不删除根目录
        if (!path.equals(rootPath)) {
            sftp.delDir(pwd);
            log.info("目录[{}]已删除", pwd);
        }
    }

    private void copyFile(Sftp sftp, String pwd, String fileName) {
        String srcFile = pwd + "/" + fileName;
        String parentPath = pwd;
        //替换掉根路径,变成相对路径 (这里取巧，不论是斜杠还是反斜杠，不会影响路径长度，可以直接截取)
        String relativePath = parentPath.substring(rootPath.length());
        //设置目标路径
        String targetPath = dowloadConfig.getPath();
        //拼接targetPath
        File targetPathFile = new File(targetPath, relativePath);

        if (!sftp.exist(srcFile)) {
            log.error("文件[{}]在[{}]中不存在，请检查", srcFile, sftpConfig.getHost());
            return;
        }
        if (!targetPathFile.exists()) {
            //如果目录不存在创建目录
            targetPathFile.mkdirs();
        }

        File targetFile = new File(targetPathFile, fileName);
        //检查数据库中是否存在
        AudioFile audioFile = audioFileMapper.selectOne(Wrappers.<AudioFile>lambdaQuery().eq(AudioFile::getFilePath, targetFile.getAbsolutePath()));

        if (targetFile.exists()) {
            log.info("文件[{}]在当前服务器中已存在，", targetFile.getAbsolutePath());
        }

        if (audioFile != null) {
            log.info(audioFile.toString());
            log.info("文件[{}]在当前服务器中已存在，且数据库中也存在，可能由于文件没有复制完成，服务停机导致", targetFile.getAbsolutePath());

            targetFile.delete();
            log.info("已删除旧文件");
        } else {
            audioFile = new AudioFile();
        }

        log.info("开始   文件[{}]传输", srcFile);
        sftp.download(srcFile, targetFile);
        log.info("结束   文件[{}]传输", srcFile);

        log.info("删除源文件[{}]", srcFile);
        sftp.delFile(srcFile);

        //保存或者更新文件到数据库
        audioFile
                .setFilePath(targetFile.getAbsolutePath())
                .setFileName(targetFile.getName());

        if (audioFile.getId() != null) {
            audioFileMapper.updateById(audioFile);
        } else {
            audioFileMapper.insert(audioFile);
        }
    }
}
