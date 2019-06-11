package com.knowledge.common.factory.watermark.support;

import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;

@Data
public class SourceImage {
    private Graphics2D graphics;
    private int sourceWidth;
    private int sourceHeight;
    private BufferedImage bufferedImage;
}
