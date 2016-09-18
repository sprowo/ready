package com.prowo.dynamic.webext.core.web.interceptor;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.prowo.ydnamic.record.Recorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 记录每次请求
 */
public class RequestRecordHandlerInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(RequestRecordHandlerInterceptor.class);

	private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-StartTime");
	
	private JmsTemplate template;    
	
	private Destination destination; 

	public JmsTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		long beginTime = System.currentTimeMillis();// 1、开始时间
		startTimeThreadLocal.set(beginTime);// 线程绑定变量（该数据只有当前请求的线程可见）
		return true;// 继续流程
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		long endTime = System.currentTimeMillis();// 2、结束时间
		long beginTime = startTimeThreadLocal.get();// 得到线程绑定的局部变量（开始时间）
		long totalTimestamp = endTime - beginTime;// 3、总的消耗的时间
		
		Recorder.setTotalTimestamp(totalTimestamp);
		
		if (totalTimestamp > 3000) {
			// 此处认为处理时间超过3秒的请求为慢请求
			// 记录到日志文件
			logger.warn(String.format("%s cost %d millis", request.getRequestURI(), totalTimestamp));
		}
		
		template.send(destination, new MessageCreator() {    
			public Message createMessage(Session session) throws JMSException {      
				return session.createObjectMessage(Recorder.getCurrentRecorder()); 
			}  
		 });    
		
		Recorder.remove();
	}

}
