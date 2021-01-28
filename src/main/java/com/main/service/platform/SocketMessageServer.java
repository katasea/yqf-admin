package com.main.service.platform;

import com.main.pojo.platform.NoticeInfo;

public interface SocketMessageServer {
	void sentAll(NoticeInfo noticeInfo);
}
