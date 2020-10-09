package com.example.audioservice.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.audioservice.mapper.AudioFileMapper;
import com.example.audioservice.model.AudioFile;
import com.example.audioservice.model.vo.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Date;

/**
 * @author Ruby
 * @create 2020-10-02 09:44:03
 */
@Api
@Slf4j
@Validated
@RestController
@RequestMapping("/audio")
public class AudioFileController {

    @Autowired
    private AudioFileMapper audioFileMapper;

    //删
    @DeleteMapping("{id}")
    public R deleleAudioFileById(@NotNull(message = "音频id不能为空") @PathVariable Integer id) {
        AudioFile audioFile = audioFileMapper.selectById(id);
        if (audioFile == null) {
            throw new RuntimeException("id不存在,无法删除");
        }
        File file = new File(audioFile.getFilePath());
        if (file.exists()) {
            file.delete();
            log.info("文件[{}]已删除",file.getAbsolutePath());
        }
        audioFileMapper.deleteById(id);

        return R.ok("删除成功");
    }

    //查询一条记录
    @GetMapping("{id}")
    public R findAudioFileById(@NotNull(message = "音频id不能为空") @PathVariable Integer id) {
        AudioFile audioFile = audioFileMapper.selectById(id);
        return R.ok(audioFile);
    }

    //查询多条记录
    @GetMapping("list")
    public R findAdudioFileByPageAndCondition(
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date end,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit
    ) {
        if (limit > 1000) {
            limit = 1000;
        }

        Page<AudioFile> audioFilePage = new Page<>(page, limit);
        audioFileMapper.selectPage(audioFilePage,
                Wrappers.<AudioFile>lambdaQuery()
                        .ge(start != null, AudioFile::getCreateTime, start)
                        .le(end != null, AudioFile::getCreateTime, end)
        );
        return R.ok(audioFilePage.getTotal(), audioFilePage.getRecords());
    }
}
