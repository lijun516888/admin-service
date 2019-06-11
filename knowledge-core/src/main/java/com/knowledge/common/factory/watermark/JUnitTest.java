package com.knowledge.common.factory.watermark;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class JUnitTest {

    public static void main(String[] args) throws Exception {
        /*PictureFactory<TextWaterMark> factory = new GenericPictureFactory<>();
        TextWaterMark product = factory.produce(TextWaterMark.class);
        InputStream is = new FileInputStream("d:/picture/dlt.jpg");
        product.makeWaterMark(is, "火箭队", "d:/picture/dlt1.jpg");*/

        // Thumbnails.of(new File("d:/picture/dlt.jpg")).size(120, 120).toFile(new File("d:/picture/dlt2.jpg"));
        BufferedImage bg = ImageIO.read(new File("d:/picture/sqhy1.jpg"));
        Graphics2D graphics = bg.createGraphics();
        graphics.setColor(Color.white);
        graphics.setFont(new Font("微软雅黑", Font.CENTER_BASELINE, 20));
        graphics.drawString("金州勇士", 10, 20);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5F));
        Thumbnails.of(bg).size(120, 120).keepAspectRatio(false).toFile(new File
                ("d:/picture/sqhy2.jpg"));

    }

}
