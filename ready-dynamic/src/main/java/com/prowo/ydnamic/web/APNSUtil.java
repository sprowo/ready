package com.prowo.ydnamic.web;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.internal.Utilities;
import com.prowo.ydnamic.context.ComxContext;
import com.prowo.ydnamic.validation.Validate;
import org.apache.log4j.Logger;

import java.io.File;

public class APNSUtil {

    private String iosPushKeyPath = "ios.push.key.path";
    private String iosPushKeyPathDev = "ios.push.key.path.dev";
    private String iosPushKeyPassword = "ios.push.key.password";

    private static Logger logger = Logger.getLogger(APNSUtil.class);

    private static ComxContext context = ComxContext.getContext();

    private APNSUtil(String iosPushKeyPath, String iosPushKeyPathDev, String iosPushKeyPassword) {
        this.iosPushKeyPassword = iosPushKeyPassword;
        this.iosPushKeyPath = iosPushKeyPath;
        this.iosPushKeyPathDev = iosPushKeyPathDev;
    }

    public static APNSUtil getInstance(String iosPushKeyPath, String iosPushKeyPathDev, String iosPushKeyPassword) {
        return new APNSUtil(iosPushKeyPath, iosPushKeyPathDev, iosPushKeyPassword);
    }

    /**
     * IOS 消息推送 -- 后期完善
     *
     * @param message     消息内容
     * @param deviceToken 设备Token
     * @param jsonMessage 消息对象信息
     * @param badge       消息数
     */
    public void push(String message, String deviceToken, String jsonMessage, int badge) {
        String keyPath = context.get(iosPushKeyPath);
        String keyPathDev = context.get(iosPushKeyPathDev);
        String keyPassword = context.get(iosPushKeyPassword);

        if (keyPassword == null || keyPath == null) {
            logger.error("推送失败:签名文件或者密码没找到");
            return;
        }
        File f = null;
        if (!Validate.isNull(keyPathDev)) {
            f = new File(keyPathDev);
            if (f.exists()) {
                ApnsService service = APNS.newService().withCert(keyPathDev, keyPassword).withSandboxDestination()
                        .build();
                String payload = APNS.newPayload().badge(badge).sound("default").alertBody(message)
                        .customField("message", jsonMessage).build();
                service.push(deviceToken, payload);
                logger.info("推送成功");
                return;
            }
        } else {
            f = new File(keyPath);
            if (f.exists()) {
                ApnsService service = APNS.newService().withCert(keyPath, keyPassword)
                        .withGatewayDestination(Utilities.PRODUCTION_GATEWAY_HOST, Utilities.PRODUCTION_GATEWAY_PORT)
                        .build();
                String payload = APNS.newPayload().badge(badge).sound("default").alertBody(message)
                        .customField("message", jsonMessage).build();
                service.push(deviceToken, payload);
                logger.info("推送成功");
                return;
            }
        }
    }
}
