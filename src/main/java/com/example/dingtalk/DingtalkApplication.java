package com.example.dingtalk;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.app.api.GenericEventListener;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;
import com.example.dingtalk.Constant;
import com.example.dingtalk.LeaveMySQL;




@SpringBootApplication
public class DingtalkApplication {

	public static void main(String[] args) {
		try {
			OpenDingTalkStreamClientBuilder
			  .custom()
			  .credential(new AuthClientCredential(Constant.appKey, Constant.appSecret))
			  //注册事件监听
			  .registerAllEventListener(new GenericEventListener() {
				public EventAckStatus onEvent(GenericOpenDingTalkEvent event) {
				  try {
					//事件唯一Id
					String eventId = event.getEventId();
					//事件类型
					String eventType = event.getEventType();
					//事件产生时间
					Long bornTime = event.getEventBornTime();
					//获取事件体
					JSONObject bizData = event.getData();
					
					// write into database
					LeaveMySQL controller = new LeaveMySQL();
					controller.connect();
					controller.ensureTableExists(eventType);
					controller.insertOrUpdateData(bizData, eventType);
					//printout
					System.out.println(eventType);
					System.out.println(bizData.toString());
					//消费成功
					return EventAckStatus.SUCCESS;
				  } 
				  
				  catch (Exception e) {
					
					//消费失败
					return EventAckStatus.LATER;
				  }
				}
			  })
			  .build().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

}
