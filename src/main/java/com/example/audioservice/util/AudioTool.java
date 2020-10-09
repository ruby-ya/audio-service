package com.example.audioservice.util;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Ruby
 * @create 2020-10-09 21:44:35
 */
@Slf4j
public class AudioTool {

    /**
     * 读取音频文件并返回时长
     * @param path
     * @return
     */
    public static double getAudioTime(String path) {
        try {
            File file = new File(path);
            Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            clip.open(ais);
            return clip.getMicrosecondLength() / 1000000.00;
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            log.info("读取文件失败[{}]", path);
        }
        return 0d;
    }
}
