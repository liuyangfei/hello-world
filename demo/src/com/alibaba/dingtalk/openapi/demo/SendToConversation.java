package com.alibaba.dingtalk.openapi.demo;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.dingtalk.openapi.demo.auth.AuthHelper;
import com.alibaba.dingtalk.openapi.demo.department.DepartmentHelper;
import com.alibaba.dingtalk.openapi.demo.message.LightAppMessageDelivery;
import com.alibaba.dingtalk.openapi.demo.message.MessageHelper;
import com.alibaba.dingtalk.openapi.demo.message.MessageHelper.Receipt;
import com.alibaba.dingtalk.openapi.demo.user.UserHelper;
import com.alibaba.fastjson.JSON;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.dingtalk.open.client.api.model.corp.CorpUserDetailList;
import com.dingtalk.open.client.api.model.corp.Department;
import com.dingtalk.open.client.api.model.corp.MessageBody;
import com.dingtalk.open.client.api.model.corp.MessageType;

public class SendToConversation {

	public static void main(String[] args) throws Exception {

		getDepartment();
		
//		send(Vars.DEVELOPMENT_GROUP, "http://www.ininin.com/", "这是开发在测试，请无视", "这是开发在测试，请无视");
		
//		send(Vars.PRODUCT_MANAGER_GROUP, "http://www.ininin.com/", "这是开发在测试，请无视", "这是开发在测试，请无视");

		String mobile = "15989417972";
		getDesigner("4021986", mobile);
		
	}

	private static void getDesigner(String departmentId, String mobile) throws Exception {
		// 获取access token
		String accessToken = AuthHelper.getAccessToken();
		log("成功获取access token: ", accessToken);
				
		String userId = "";
		// 获取部门成员
		CorpUserDetailList userList = UserHelper.getUserDetails(accessToken, Long.valueOf(departmentId), null, null, null);
		for (CorpUserDetail user : userList.getUserlist()) {
			System.out.println(user.getMobile());
			if (mobile.equals(user.getMobile())) {
				userId = user.getUserid();
				break;
			}
		}
		log("成功获取userId: ", userId);
		//send(userId, "", "http://www.ininin.com/", "这是开发在测试，请无视", "这是开发在测试，请无视");
	}

	private static void send(String toUsers, String toParties, String messageUrl, String title, String text) throws Exception {
		// 获取access token
		String accessToken = AuthHelper.getAccessToken();
		log("成功获取access token: ", accessToken);

		// 发送微应用消息
		String agentId = Vars.AGENT_ID;
		LightAppMessageDelivery lightAppMessageDelivery = new LightAppMessageDelivery(toUsers, toParties, agentId);

		MessageBody.LinkBody linkBody = new MessageBody.LinkBody();
		linkBody.setMessageUrl(messageUrl);
		linkBody.setPicUrl("@lALOACZwe2Rk");
		linkBody.setTitle(title);
		linkBody.setText(text);

		lightAppMessageDelivery.withMessage(MessageType.LINK, linkBody);
		Receipt r2 = MessageHelper.send(accessToken, lightAppMessageDelivery);
		log("发送 微应用link消息，" + JSON.toJSONString(r2));
	}

	private static void getDepartment() throws Exception {
		List<Department> departments = new ArrayList<Department>();
		departments = DepartmentHelper.listDepartments(AuthHelper.getAccessToken(), "1");
		log(JSON.toJSON(departments));
	}

	private static void log(Object... msgs) {
		StringBuilder sb = new StringBuilder();
		for (Object o : msgs) {
			if (o != null) {
				sb.append(o.toString());
			}
		}
		System.out.println(sb.toString());
	}

}
