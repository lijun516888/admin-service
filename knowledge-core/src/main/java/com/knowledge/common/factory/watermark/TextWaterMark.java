package com.knowledge.common.factory.watermark;

import com.knowledge.common.factory.watermark.support.SourceImage;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TextWaterMark extends AbstractWaterMark {

    public static final String FONT_NAME = "微软雅黑";
    public static final int FONT_STYLE = Font.BOLD;	// 黑体
    public static final int FONT_SIZE = 20;			// 文字大小
    public static final Color FONT_COLOR = Color.red; // 文字颜色
    public static float ALPHA = 0.5F; //文字水印透明度


    public void makeWaterMark(InputStream source, String text, String savePath) {
        FileOutputStream os = null;
        try {
            SourceImage si = readSourceImage(source);
            Graphics2D graphics = si.getGraphics();
            // 4 使用绘图工具对象将水印（文字/图片）绘制到缓存图片
            graphics.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
            graphics.setColor(FONT_COLOR);
            // 文字水印宽度
            int textWaterWidth = FONT_SIZE * getTextLength(text);
            // 文字水印高度
            int textWaterHeight = FONT_SIZE;
            // 计算原图和文字水印的宽度和高度之差
            int x = si.getSourceWidth() - textWaterWidth; // 宽度之差
            int y= si.getSourceHeight() - textWaterHeight;	// 高度之差
            // 水印透明度的设置
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));

            // 旋转图片(30°)
            graphics.rotate(Math.toRadians(30), x, y);

            // 绘制文字
            graphics.drawString(text, x, y);
            // 释放工具
            graphics.dispose();
            // 创建文件输出流，指向最终的目标文件
            os = new FileOutputStream(savePath);
            // 5 创建图像文件编码工具类
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
            // 6 使用图像编码工具类，输出缓存图像到目标文件
            en.encode(si.getBufferedImage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null) {
                try {
                    // 关闭流
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
