package com.main.service.platform.impl;

import com.common.*;
import com.main.dao.platform.NoticeInfoDao;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.NoticeInfo;
import com.main.pojo.platform.Bridge;
import com.main.service.platform.NoticeInfoService;
import com.main.service.platform.SocketMessageServer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象服务层实现类
 * @author Tim[ATC.Pro Generate]
 */
@Service
@Transactional
public class NoticeInfoServiceImpl implements NoticeInfoService {
	@Resource
	private NoticeInfoDao noticeInfoDao;

	@Resource
	private SocketMessageServer sms;

	@Override
	public NoticeInfo selectBypkid(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.noticeInfoDao.selectBypkid(map);
	}
	
	@Override
    public int getCount(String keyword,String node,Bridge bridge,String view,String type) {
        Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("view", CommonUtil.nullToZero(view));
		map.put("userid", bridge.getUserid());
		map.put("state", type);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
        Map<String,Object> resultMap = this.noticeInfoDao.getCount(map);
        return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
    }

	@Override
	public List<NoticeInfo> get(String keyword, String node, Bridge bridge, String start, String limit, String view, String type) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("view", CommonUtil.nullToZero(view));
		map.put("userid", bridge.getUserid());
		map.put("state", type);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
        map.put("start", start);
		map.put("end", Integer.parseInt(start)+Integer.parseInt(limit));
		return this.noticeInfoDao.getPage(map);
	}
	@Override
	public StateInfo delete(String code,String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(code)) {
			try {
				Map<String,Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id",code);
				//顺序不能乱
				this.noticeInfoDao.deleteByPrimaryKey(map);
				//插入用户与通知对应关系 0 表示未读 1 表示已读
				this.noticeInfoDao.deleteUserNoticeRela(code);
			}catch(Exception e){
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(),e.getMessage(),e);
			}
		}else {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),"传递的id为空无法删除，请刷新后重试！",null);
		}
		return stateInfo;
	}
	/**
	 * 获取bean 信息的map 用于传递给mybatis
	 */
	private Map<String,Object> getBeanInfoMap(NoticeInfo noticeInfo, Bridge bridge){
		
		Map<String,Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("pkid",noticeInfo.getPkid());
		map.put("title",noticeInfo.getTitle());
		map.put("content",noticeInfo.getContent());
		map.put("senttime",noticeInfo.getSenttime());
		map.put("fromwho",noticeInfo.getFromwho());
		map.put("fromwhoname",noticeInfo.getFromwhoname());
		map.put("type",noticeInfo.getType());
		return map;
	}
	@Override
	public StateInfo edit(String pkid, NoticeInfo noticeInfo, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(pkid) && noticeInfo != null) {
			Map<String,Object> map = this.getBeanInfoMap(noticeInfo,bridge);
			map.put("whereId",pkid);
			try {
				this.noticeInfoDao.update(map);
				//插入用户与通知对应关系 0 表示未读 1 表示已读
				this.saveUserNoticeRela(noticeInfo,bridge,0);
			}catch(Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(),e.getMessage(),e);
			}
		}else {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),"传递的关键字段为空或者对象不存在，请刷新页面重试",null);
		}
		return stateInfo;
	}


	@Override
	public StateInfo add(NoticeInfo noticeInfo, Bridge bridge) {
		//tree init
		//自动生成主键按1 101 10101规则
		Map<String,Object> mapOfID = this.noticeInfoDao.getAutoGeneralID(null,bridge);
		noticeInfo.setPkid(CommonUtil.dealPKRule(mapOfID,null));
		Map<String,Object> map = this.getBeanInfoMap(noticeInfo,bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			this.noticeInfoDao.insert(map);
			//插入用户与通知对应关系 0 表示未读 1 表示已读
			this.saveUserNoticeRela(noticeInfo,bridge,0);
		}catch(Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	@Override
	public StateInfo saveUserNoticeRela(NoticeInfo noticeInfo, Bridge bridge, int state) {
		StateInfo stateInfo = new StateInfo();
		try {
			noticeInfoDao.saveUserNoticeRela(noticeInfo,bridge,state);
			if(state == 0) {
				sms.sentAll(noticeInfo);
			}
		}catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),null,e);
		}
		return stateInfo;
	}

	@Override
	public List<NoticeInfo> getNoticeWithState(String userid, Bridge bridge, int state) {
		return noticeInfoDao.getNoticeWithState(userid,bridge,state);
	}

	@Override
	public StateInfo hadread(String pkid, String userid) {
		StateInfo stateInfo = new StateInfo();
		try {
			int state = 1;
			noticeInfoDao.updateUserNoticeState(userid,pkid,state);
		}catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),null,e);
		}
		return stateInfo;
	}

}