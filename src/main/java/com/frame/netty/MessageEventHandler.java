package com.frame.netty;

import com.common.Global;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.main.pojo.platform.NoticeInfo;
import com.main.pojo.platform.UserInfo;
import com.main.service.platform.SocketMessageServer;
import com.main.service.platform.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Component
public class MessageEventHandler {

	private final SocketIOServer server;

	@Resource
	private SocketMessageServer smessage;
	@Resource
	private UserInfoService userInfoService;

	@Autowired
	public MessageEventHandler(SocketIOServer server) {
		this.server = server;
	}

	//添加connect事件，当客户端发起连接时调用，本文中将clientid与sessionid存入数据库
	//方便后面发送消息时查找到对应的目标client,
	@OnConnect
	public void onConnect(SocketIOClient client) {
		String clientId = client.getHandshakeData().getSingleUrlParam("clientid");
		UserInfo userInfo = userInfoService.selectByUserid(null,clientId);
		if (userInfo != null) {
			Date nowTime = new Date(System.currentTimeMillis());
			userInfo.setConnected((short) 1);
			userInfo.setMostsignbits(client.getSessionId().getMostSignificantBits());
			userInfo.setLeastsignbits(client.getSessionId().getLeastSignificantBits());
			userInfo.setLastconnecteddate(Global.df.format(nowTime));
			userInfoService.editSocketInfo(userInfo);
		}
	}

	//添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
	@OnDisconnect
	public void onDisconnect(SocketIOClient client) {
		String clientId = client.getHandshakeData().getSingleUrlParam("clientid");
		UserInfo userInfo = userInfoService.selectByUserid(null,clientId);
		if (userInfo != null) {
			userInfo.setConnected((short) 0);
			userInfo.setMostsignbits((long)0);
			userInfo.setLeastsignbits((long)0);
			userInfoService.editSocketInfo(userInfo);
		}
	}

	//消息接收入口，当接收到消息后，查找发送目标客户端，并且向该客户端发送消息，且给自己发送消息
	@OnEvent(value = "messageevent")
	public void onEvent(SocketIOClient client, AckRequest request, NoticeInfo data) {
		String targetClientId = data.getTowho();
		UserInfo targetUser = userInfoService.selectByUserid(null,targetClientId);
		if (targetUser != null && targetUser.getConnected() != 0) {
			UUID uuid = new UUID(targetUser.getMostsignbits(), targetUser.getLeastsignbits());
			MessageInfo sendData = new MessageInfo();
			sendData.setSourceClientId(data.getFromwho());
			sendData.setTargetClientId(data.getTowho());
			sendData.setMsgType(String.valueOf(data.getType()));
			sendData.setMsgContent(data.getContent());
			client.sendEvent("messageevent", sendData);
			server.getClient(uuid).sendEvent("messageevent", sendData);
		}
	}
}

