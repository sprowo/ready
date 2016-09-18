package com.prowo.dynamic.webext.common.jms;

import com.prowo.ydnamic.mapper.IDGenerator;
import com.prowo.ydnamic.persist.ComxPagingDAO;
import com.prowo.ydnamic.record.RecordVO;
import com.prowo.ydnamic.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Date;

public class RecordMessageListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(RecordMessageListener.class);

    private ComxPagingDAO cadao;

    public ComxPagingDAO getCadao() {
        return cadao;
    }

    public void setCadao(ComxPagingDAO cadao) {
        this.cadao = cadao;
    }

    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            try {
                RecordVO recorder = (RecordVO) ((ObjectMessage) message).getObject();
                String requestData = recorder.getRequestData();
                String tempResponseData = recorder.getTempResponseData();
                String tempRequestData = recorder.getTempResponseData();
                String serviceErrorCode = recorder.getServiceErrorcode();
                String referer = recorder.getReferer();
                String userAgent = recorder.getUserAgent();
                if (recorder.isServiceResult()) {
                    if (!Validate.isNull(requestData) && requestData.length() > 50) {
                        requestData = requestData.substring(0, 50) + "...";
                    }
                } else {
                    if (!Validate.isNull(requestData) && requestData.length() > 200) {
                        requestData = requestData.substring(0, 200) + "...";
                    }
                }
                if (!Validate.isNull(tempResponseData) && tempResponseData.length() > 200) {
                    tempResponseData = tempResponseData.substring(0, 200) + "...";
                }
                if (!Validate.isNull(tempRequestData) && tempRequestData.length() > 200) {
                    tempRequestData = tempRequestData.substring(0, 200) + "...";
                }

                if (!Validate.isNull(serviceErrorCode) && serviceErrorCode.length() > 50) {
                    serviceErrorCode = serviceErrorCode.substring(0, 50) + "...";
                }

                if (!Validate.isNull(referer) && referer.length() > 200) {
                    referer = referer.substring(0, 200) + "...";
                }

                if (!Validate.isNull(userAgent) && userAgent.length() > 200) {
                    userAgent = userAgent.substring(0, 200) + "...";
                }

                if (recorder.isOpLog()) {
                    cadao.update(C_OP_LOGRECORD_ADD, new Object[]{IDGenerator.getUUID(), requestData, referer,
                            userAgent, recorder.getCustomerIp(), recorder.getServiceId(), recorder.isServiceResult(),
                            serviceErrorCode, recorder.getTotalTimestamp(), recorder.getPartnerid(), new Date()});
                } else {
                    cadao.update(
                            RECORD_ADD,
                            new Object[]{IDGenerator.getUUID(), recorder.getRequestid(), requestData,
                                    tempRequestData, tempResponseData, recorder.getCustomerId(),
                                    recorder.getCustomerIp(), recorder.getServiceId(), recorder.isServiceResult(),
                                    serviceErrorCode, recorder.getTotalTimestamp(), recorder.getRequestTimestamp(),
                                    new Date(), recorder.getVersion(), recorder.getPartnerid()});
                }

            } catch (JMSException e) {
                logger.error("JMS异常", e);
            } catch (Exception e) {
                logger.error("保存记录失败", e);
            }
        }
    }

    // 接口访问记录管理
    private static final String RECORD_ADD = "INSERT INTO c_records(c_id, request_id, request_data, temp_request_data, temp_response_data, customer_id, customer_ip, service_id, service_result, service_errorcode, total_millseconds, request_millseconds, create_time, version, partner_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String C_OP_LOGRECORD_ADD = "INSERT INTO c_op_log(c_id, request_data, referer, user_agent, customer_ip, service_id, service_result, service_errorcode, total_millseconds, partner_id, create_time) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

}
