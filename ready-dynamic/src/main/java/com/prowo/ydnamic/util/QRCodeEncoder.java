package com.prowo.ydnamic.util;

import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;
import com.prowo.ydnamic.validation.Validate;
import com.swetake.util.Qrcode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class QRCodeEncoder {

    public BufferedImage encoderQRCode(String content, Map<String, Object> options) {
        BufferedImage bufImg = new BufferedImage(280, 280, BufferedImage.TYPE_INT_RGB);
        try {

            Qrcode qrcodeHandler = new Qrcode();
            qrcodeHandler.setQrcodeErrorCorrect(Validate.nonNullVal(options, "encodeCorrect", 'M'));
            qrcodeHandler.setQrcodeEncodeMode(Validate.nonNullVal(options, "encodeMode", 'B'));
            qrcodeHandler.setQrcodeVersion(Validate.nonNullVal(options, "version", 18));


            byte[] contentBytes = content.getBytes(Validate.nonNullVal(options, "charset", "gb2312"));

            Graphics2D gs = bufImg.createGraphics();
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, 280, 280);
            // 设定图像颜色 BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量 不设置可能导致解析出错
            int pixoff = 4;

            // 输出内容 二维码
            if (contentBytes.length > 0 && contentBytes.length < 512) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 4 + pixoff, i * 4 + pixoff, 4, 4);
                        }
                    }
                }
            } else {
                LoggerUtil.log(Level.WARN, "QRCode content bytes length =" + contentBytes.length + "not in [ 0,512 ]");
            }
            gs.dispose();
            bufImg.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImg;
    }
}
