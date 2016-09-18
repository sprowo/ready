package com.prowo.ydnamic.record;

import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;

public class Recorder {

    private static ThreadLocal<RecordVO> recordLocal = new ThreadLocal<RecordVO>();

    public static String getPartnerid() {
        return getCurrentRecorder().getPartnerid();
    }

    public static RecordVO getCurrentRecorder() {
        RecordVO recordVO = recordLocal.get();
        if (recordVO == null) {
            recordVO = new RecordVO();
            recordLocal.set(recordVO);
        }
        return recordVO;
    }

    public static void remove() {
        recordLocal.remove();
    }

    public static void setVersion(String version) {
        getCurrentRecorder().setVersion(version);
    }

    public static void setRequestid(String requestid) {
        getCurrentRecorder().setRequestid(requestid);
    }

    public static void setRequestData(String requestData) {
        LoggerUtil.log(Level.DEBUG, "{0}", requestData);
        getCurrentRecorder().setRequestData(requestData);
    }

    public static void setReferer(String referer) {
        getCurrentRecorder().setReferer(referer);
    }

    public static void setUserAgent(String userAgent) {
        getCurrentRecorder().setUserAgent(userAgent);
    }

    public static void setCustomerId(String customerId) {
        getCurrentRecorder().setCustomerId(customerId);
    }

    public static void setCustomerIp(String customerIp) {
        getCurrentRecorder().setCustomerIp(customerIp);
    }

    public static void setServiceId(String serviceId) {
        getCurrentRecorder().setServiceId(serviceId);
    }

    public static void setServiceResult(boolean serviceResult) {
        getCurrentRecorder().setServiceResult(serviceResult);
    }

    public static void setTotalTimestamp(long totalTimestamp) {
        getCurrentRecorder().setTotalTimestamp(totalTimestamp);
    }

    public static void setRequestTimestamp(long requestTimestamp) {
        getCurrentRecorder().setRequestTimestamp(requestTimestamp);
    }

    public static void setPartnerid(String partnerid) {
        getCurrentRecorder().setPartnerid(partnerid);
    }

    public static void setTempRequestData(String tempRequestData) {
        getCurrentRecorder().setTempRequestData(tempRequestData);
    }

    public static void setTempResponseData(String tempResponseData) {
        getCurrentRecorder().setTempResponseData(tempResponseData);
    }

    public static void setServiceErrorcode(String serviceErrorcode) {
        getCurrentRecorder().setServiceErrorcode(serviceErrorcode);
    }

    public static void setOpLog(Boolean opLog) {
        getCurrentRecorder().setOpLog(opLog);
    }
}
