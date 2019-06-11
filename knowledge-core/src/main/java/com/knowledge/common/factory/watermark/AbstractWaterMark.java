package com.knowledge.common.factory.watermark;

import com.knowledge.common.factory.watermark.support.SourceImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractWaterMark implements WaterMark {

    SourceImage readSourceImage(InputStream source) throws IOException {
        SourceImage sourceImage = new SourceImage();
        // 1 创建图片缓存对象
        // 解码原图
        Image target = ImageIO.read(source);
        // 获取原图的宽度
        int targetWidth = target.getWidth(null);
        // 获取原图的宽度
        int targetHeight = target.getHeight(null);
        // 图像颜色的设置
        BufferedImage bufferedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        // 2 创建Java绘图工具对象
        Graphics2D graphics = bufferedImage.createGraphics();
        // 3 使用绘图工具对象将原图绘制到缓存图片对象
        graphics.drawImage(target, 0, 0, targetWidth, targetHeight, null);
        sourceImage.setGraphics(graphics);
        sourceImage.setSourceHeight(targetHeight);
        sourceImage.setSourceWidth(targetWidth);
        sourceImage.setBufferedImage(bufferedImage);
        return sourceImage;
    }

    /**
     * 文本长度的处理：文字水印的中英文字符的宽度转换
     * @param text
     * @return
     */
    public int getTextLength(String text) {
        int length = text.length();
        for (int i = 0; i < text.length(); i++) {
            String s = String.valueOf(text.charAt(i));
            //中文字符
            if (s.getBytes().length > 1) {
                length++;
            }
        }
        //中文和英文字符的转换
        length = length % 2 == 0 ? length / 2 : length / 2 + 1;
        return length;
    }

}
