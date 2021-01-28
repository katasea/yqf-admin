package com.main.service.platform.impl;

import com.common.Global;
import com.corundumstudio.socketio.SocketIOClient;
import com.main.pojo.platform.NoticeInfo;
import com.main.service.platform.SocketMessageServer;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SocketMessageServerImpl implements SocketMessageServer {

	@Override
	public void sentAll(NoticeInfo noticeInfo) {
		Collection<SocketIOClient> clients = Global.NETTY_SOCKET_SERVER.getAllClients();
		for (SocketIOClient s : clients) {
			s.sendEvent("messageevent", noticeInfo);
		}
	}
}
