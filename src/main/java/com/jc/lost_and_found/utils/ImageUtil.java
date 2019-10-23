package com.jc.lost_and_found.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * 图片压缩工具类
 *
 **/
@Slf4j
public class ImageUtil {

    // 图片默认缩放比率
    private static final int DEFAULT_WIDTH = 256;
    private static final int DEFAULT_HEIGHT = 256;

    public static byte[] makeByBufferImage(byte[] bytes, String fileName) throws IOException {
        BufferedImage bufferedImage = bytesToBufferedImage(bytes);
        if(bufferedImage == null){
            return bytes;
        }
        double height = bufferedImage.getHeight();
        double width = bufferedImage.getWidth();
        if(height <= DEFAULT_HEIGHT && width <= DEFAULT_WIDTH){
            return bytes;
        }
        double scale = Math.min(DEFAULT_HEIGHT / height, DEFAULT_WIDTH / width);
        log.info("压缩比例：" + scale);
        BufferedImage rs = Thumbnails.of(bufferedImage).scale(scale).asBufferedImage();
        return imageToBytes(rs, fileName);
    }

    private static byte[] imageToBytes(BufferedImage bufferedImage, String fileName){
        String suffix = "";
        if(fileName.contains(".")){
            suffix = fileName.substring(fileName.indexOf('.') + 1);
        }
        if(StringUtils.isEmpty(suffix)){
            suffix = "jpg";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, suffix, baos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return baos.toByteArray();
    }

    private static BufferedImage bytesToBufferedImage(byte[] bytes){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try {
            return ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
