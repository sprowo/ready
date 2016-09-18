package com.prowo.ydnamic.record;

import com.prowo.persist.BaseVO;

public class RecordVO extends BaseVO {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public RecordVO() {
        super();
        this.serviceResult = true;
    }

    private String version;

    private String requestid;

    private String requestData;

    private String referer;

    private String userAgent;

    private String tempRequestData;

    private String tempResponseData;

    private String customerId;

    private String customerIp;

    private String serviceId;

    private boolean serviceResult;

    private String serviceErrorcode;

    private long totalTimestamp;

    private long requestTimestamp;

    private String partnerid;

    private boolean opLog = false;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerIp() {
        return customerIp;
    }

    public void setCustomerIp(String customerIp) {
        this.customerIp = customerIp;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public boolean isServiceResult() {
        return serviceResult;
    }

    public void setServiceResult(boolean serviceResult) {
        this.serviceResult = serviceResult;
    }

    public long getTotalTimestamp() {
        return totalTimestamp;
    }

    public void setTotalTimestamp(long totalTimestamp) {
        this.totalTimestamp = totalTimestamp;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getTempRequestData() {
        return tempRequestData;
    }

    public void setTempRequestData(String tempRequestData) {
        this.tempRequestData = tempRequestData;
    }

    public String getTempResponseData() {
        return tempResponseData;
    }

    public void setTempResponseData(String tempResponseData) {
        this.tempResponseData = tempResponseData;
    }

    public String getServiceErrorcode() {
        return serviceErrorcode;
    }

    public void setServiceErrorcode(String serviceErrorcode) {
        this.serviceErrorcode = serviceErrorcode;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setOpLog(boolean opLog) {

        this.opLog = opLog;
    }

    public boolean isOpLog() {
        return opLog;
    }

}
