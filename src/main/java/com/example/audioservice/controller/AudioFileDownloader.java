package com.example.audioservice.controller;

import com.example.audioservice.mapper.AudioFileMapper;
import com.example.audioservice.model.AudioFile;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author Ruby
 * @create 2020-10-02 09:40:58
 */
@Api
@Validated
@Controller
@RequestMapping("/download")
public class AudioFileDownloader {

    @Autowired
    private AudioFileMapper audioFileMapper;

    /**
     * 使用HttpResponse输出内容，防止内存移除
     */
    @GetMapping("/audio/{id}")
    public void download(@NotNull(message = "音频id不能为空") @PathVariable Integer id,
                         HttpServletResponse response
    ) {
        AudioFile audioFile = audioFileMapper.selectById(id);
        if (audioFile == null) {
            outputBlank(response);
            return;
        }
        File file = new File(audioFile.getFilePath());
        if (file.exists()) {
            try (
                    BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
                    BufferedOutputStream bout = new BufferedOutputStream(response.getOutputStream());
            ) {
                //1.设置响应头
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(audioFile.getFileName(), "UTF-8"));
                response.addHeader("Content-Length", "" + file.length());
                response.setContentType("application/octet-stream");

                //2.遍历输出文件内容

                int b;//中间变量
                while ((b = bin.read()) != -1) {
                    bout.write(b);
                }

            } catch (Exception e) {
                outputBlank(response);
                return;
            }
        }

    }

    private void outputBlank(HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html");
        out.print("<p>");
        out.print("File not Found");
        out.print("</p>");
    }
}
