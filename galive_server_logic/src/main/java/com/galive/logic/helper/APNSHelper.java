package com.galive.logic.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import com.galive.logic.config.APNSConfig;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.service.UserServiceImpl;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class APNSHelper {

	private boolean isDistribution;
	private String cert;
	private String password;
	private APNSConfig config;
	private UserServiceImpl userService = new UserServiceImpl();
	
	public APNSHelper(boolean isDistribution) {
		this.isDistribution = isDistribution;
		config = ApplicationConfig.getInstance().getApnsConfig();
		String certName = isDistribution ? config.getCertNameDistruction() : config.getCertNameDevelopment();
		cert = LogicHelper.loadApnsCertPath(certName);
		password = isDistribution ? config.getCertPasswordDistruction() : config.getCertPasswordDevelopment();
	}
	
	public void push(String deviceToken, String content) {
		List<String> deviceTokens = new ArrayList<String>();
		deviceTokens.add(deviceToken);
		push(deviceTokens, content);
	}
	
	public void push(List<String> deviceTokens, String content) {
		Calendar c = DateUtils.toCalendar(new Date());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour >= 23 || hour < 8) { // 23点到8点不推送
			return;
		}
		
		ApnsService service = APNS.newService().withCert(cert, password).withAppleDestination(isDistribution).build();
		String payload = APNS.newPayload()
				//.alertTitle("alertTitle")
				.badge(config.getPushBadge())
				.alertBody(content)
				.sound(config.getPushSound())
				.build();				
		service.push(deviceTokens, payload);
		
		Map<String, Date> inactiveDevices = service.getInactiveDevices();
		// 删除无效token
		for (String deviceToken : inactiveDevices.keySet()) {
			userService.deleteUserDeviceToken(deviceToken);
		}
	}
	
	
	// Test
	public static void main(String args[]) throws Exception {
		APNSHelper apns = new APNSHelper(true);
		apns.cert = "D:\\doodduck\\apns\\cert_production.p12";
		apns.password = "superman";
		apns.push("c3560c330098ddb75a6a222261399d0b1de9f3297a320b3b48eea3cbb7b44934", "");
	}
}
