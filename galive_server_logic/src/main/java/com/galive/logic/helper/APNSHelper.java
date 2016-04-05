package com.galive.logic.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.time.DateUtils;

import com.galive.logic.config.APNSConfig;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.service.UserServiceImpl;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class APNSHelper {

	private static ExecutorService executorService = Executors.newFixedThreadPool(10);
	
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
	
	public void push(final List<String> deviceTokens, final String content) {
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				Calendar c = DateUtils.toCalendar(new Date());
				int hour = c.get(Calendar.HOUR_OF_DAY);
				if (hour >= 23 || hour < 8) { // 23点到8点不推送
					return;
				}
				
				/// TODO 当前只有生产环境有效
				ApnsService service = APNS.newService().withCert(cert, password).withAppleDestination(true).build();
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
					userService.deleteDeviceToken(deviceToken);
				}
				
			}
		});
	}
	
	
	// Test
	public static void main(String args[]) throws Exception {
		APNSHelper apns = new APNSHelper(true);
		apns.cert = "/Users/luguangqing/Downloads/cert_production.p12";
		apns.password = "1234";
		apns.push("14be385c531c07197a0b77b157c1b499d0c3084da2ceecfdff805572e86d8d3d", "asdasdas");
	}
}
