package com.prowo.ydnamic.verifcode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 功能描述：  用来生成验证码图片
 */
public class ImageValidCode {

    private static Random random = new Random();

    private static float IMAGEWIDTH = 18.5f;  // 图片宽度

    private static int IMAGEHEIGHT = 24;      // 图片高度

    private static int CODE_LENGTH = 4;       // 随机字符串的长度

    private static int FONTSIZE = random.nextInt(4) + 18;   // 字体大小

    /**
     * 生成指定字符串的图像数据
     *
     * @param verifyCode 即将被打印的随机字符串
     * @return 生成的图像数据
     */
    public static BufferedImage getImage(String verifyCode) {
        // 生成画布
        BufferedImage image = new BufferedImage((int) IMAGEWIDTH * CODE_LENGTH, IMAGEHEIGHT, BufferedImage.TYPE_INT_RGB);

        // 获取图形上下文 （生成画笔）
        Graphics graphics = image.getGraphics();

        // 设置背景色（）,填充矩形区域 ，作为背景
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, (int) IMAGEWIDTH * 4, IMAGEHEIGHT);

        // 设置边框颜色 ,画出边框
        graphics.setColor(Color.white);
        for (int i = 0; i < 2; i++)
            graphics.drawRect(i, i, (int) (IMAGEWIDTH * CODE_LENGTH - i * 2 - 1), IMAGEHEIGHT - i * 2 - 1);

        // 设置随机干扰线条颜色 ,产生50条干扰线条
        graphics.setColor(Color.white);
        for (int i = 0; i < 50; i++) {
            int x1 = random.nextInt((int) (IMAGEWIDTH * CODE_LENGTH - 4)) + 2;
            int y1 = random.nextInt((int) (IMAGEHEIGHT - 4)) + 2;
            int x2 = random.nextInt((int) (IMAGEWIDTH * CODE_LENGTH - 2 - x1)) + x1;
            int y2 = y1;
            graphics.drawLine(x1, y1, x2, y2);
        }

        for (int i = 0; i < CODE_LENGTH; i++) {
            graphics.setFont(new Font("TimesRoman", Font.BOLD, FONTSIZE)); // 设置字体
        }

        // 画字符串
        for (int i = 0; i < CODE_LENGTH; i++) {
            String temp = verifyCode.substring(i, i + 1);
            graphics.setColor(Color.black);
            if (i % 2 != 0) {
                graphics.drawString(temp, 17 * i + 4, 16);
            } else {
                graphics.drawString(temp, 17 * i + 4, 16);
            }
        }
        graphics.dispose();// 图像生效
        return image;
    }

}