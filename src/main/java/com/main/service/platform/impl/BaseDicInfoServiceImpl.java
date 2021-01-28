package com.main.service.platform.impl;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Resource;

import com.common.ZjmUtil;
import com.main.dao.platform.UserInfoDao;
import com.main.pojo.platform.BasDicInfo;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.UserInfo;
import org.springframework.stereotype.Service;
import com.common.Global;
import com.main.dao.platform.CommonDao;
import com.main.dao.platform.BaseDicInfoDao;
import com.main.service.platform.BaseDicInfoService;
import com.common.CommonUtil;
import com.common.AccessOptUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * 对象服务层实现类
 *
 * @author Tim[ATC.Pro Generate]
 */
@Service
@Transactional
public class BaseDicInfoServiceImpl implements BaseDicInfoService {
	@Resource
	private BaseDicInfoDao basDicInfoDao;
	@Resource
	private UserInfoDao userInfoDao;
	@Resource
	private CommonDao comDao;

	@Override
	public BasDicInfo selectBydicid(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.basDicInfoDao.selectBydicid(map);
	}

	@Override
	public StateInfo validator(String key, String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (!CommonUtil.isEmpty(basDicInfoDao.validator(key, value, bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), "已录入重复的dicid编号，请修改！", null);
			}
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@Override
	public int getCount(String keyword, String node, Bridge bridge) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		Map<String, Object> resultMap = this.basDicInfoDao.getCount(map);
		return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
	}

	@Override
	public List<BasDicInfo> get(String keyword, String node, Bridge bridge, String start, String limit) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		List<BasDicInfo> result = this.basDicInfoDao.get(map);
		if (CommonUtil.isEmpty(keyword)) {
			for (BasDicInfo m : result) {
				m.setId(String.valueOf(m.getDicid()));
				m.setLeaf(m.getIsleaf() == 1);
			}
		} else {
			for (BasDicInfo m : result) {
				m.setId(String.valueOf(m.getDicid()));
				m.setLeaf(true);
			}
		}
		return result;
	}

	@Override
	public StateInfo delete(String code, String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(code)) {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id", code);
				//顺序不能乱
				this.basDicInfoDao.deleteByPrimaryKey(map);
				if (!CommonUtil.isEmpty(parentId) && !"0".equals(parentId) && !"root".equals(parentId)) {
					map.put("node", parentId);
					map.put("companyid", bridge.getCompanyid());

					List<BasDicInfo> childrens = this.basDicInfoDao.get(map);
					//父节点没有子节点要把父节点的leaf 设置为0
					if (childrens == null || childrens.size() == 0) {
						Map<String, Object> map2 = new HashMap<>();
						map2.put("year", bridge.getYear());
						map2.put("id", parentId);
						map2.put("value", "1");
						//父节点leaf 设置为0
						this.basDicInfoDao.changeLeaf(map2);
					}
				}
			} catch (Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), e.getMessage(), e);
			}
		} else {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), "传递的id为空无法删除，请刷新后重试！", null);
		}
		return stateInfo;
	}

	/**
	 * 获取bean 信息的map 用于传递给mybatis
	 */
	private Map<String, Object> getBeanInfoMap(BasDicInfo basDicInfo, Bridge bridge) {

		Map<String, Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("dicid", basDicInfo.getDicid());
		map.put("dicname", basDicInfo.getDicname());
		map.put("zjm", basDicInfo.getZjm());
		map.put("dickey", basDicInfo.getDickey());
		map.put("dicval", basDicInfo.getDicval());
		map.put("parentid", basDicInfo.getParentid());
		map.put("isleaf", basDicInfo.getIsleaf());
		map.put("isstop", basDicInfo.getIsstop());
		return map;
	}

	@Override
	public StateInfo edit(String dicid, BasDicInfo basDicInfo, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(dicid) && basDicInfo != null) {
			Map<String, Object> map = this.getBeanInfoMap(basDicInfo, bridge);
			map.put("whereId", dicid);
			try {
				this.basDicInfoDao.update(map);
			} catch (Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), e.getMessage(), e);
			}
		} else {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), "传递的关键字段为空或者对象不存在，请刷新页面重试", null);
		}
		return stateInfo;
	}


	@Override
	public StateInfo add(String parentId, BasDicInfo basDicInfo, Bridge bridge) {
		//tree init
		basDicInfo.setIsleaf(1);
		if (!CommonUtil.isEmpty(parentId)) {
			basDicInfo.setParentid(parentId);
		} else {
			basDicInfo.setParentid(Global.NULLSTRING);
		}
		//自动生成主键按1 101 10101规则
		Map<String, Object> mapOfID = this.basDicInfoDao.getAutoGeneralID(parentId, bridge);
		basDicInfo.setDicid(CommonUtil.dealPKRule(mapOfID, parentId));
		basDicInfo.setZjm(ZjmUtil.generateZJM(basDicInfo.getDicname()));
		Map<String, Object> map = this.getBeanInfoMap(basDicInfo, bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			if (!CommonUtil.isEmpty(parentId)) {
				Map<String, Object> map2 = new HashMap<>();
				map2.put("year", bridge.getYear());
				map2.put("id", parentId);
				map2.put("value", "0");
				//父节点leaf 设置为0
				this.basDicInfoDao.changeLeaf(map2);
			}
			this.basDicInfoDao.insert(map);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	/**
	 * 初始化内置数据
	 */
	@Override
	public StateInfo autoInitFromAccess() {
		StateInfo stateInfo = new StateInfo();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			this.basDicInfoDao.deleteAll();
			conn = AccessOptUtil.connectAccessFile();
			if (conn != null) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("Select * From PM_DICTIONARY");
				List<String> sqlList = new ArrayList<>();
				StringBuilder sqlBuffer = new StringBuilder();
				while (rs.next()) {
					sqlBuffer.setLength(0);
					sqlBuffer.append("Insert Into PM_DICTIONARY");
					sqlBuffer.append("(dicid,dicname,zjm,dickey,dicval,parentid,isleaf,isstop) values (");
					sqlBuffer.append("'").append(rs.getString("dicid")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("dicname")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("zjm")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(CommonUtil.nullToStr(rs.getString("dickey"))).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(CommonUtil.nullToStr(rs.getString("dicval"))).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(CommonUtil.nullToStr(rs.getString("parentid"))).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append(rs.getInt("isleaf"));
					sqlBuffer.append(",");
					sqlBuffer.append(rs.getInt("isstop"));
					sqlBuffer.append(")");
					sqlList.add(sqlBuffer.toString());
				}
				comDao.transactionUpdate(sqlList);
			}
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		} finally {
			Global.close(conn, stmt, rs);
		}
		return stateInfo;
	}

	@Override
	public List<Map<String, Object>> getComboJson(String type) {
		if(!CommonUtil.isEmpty(type)) {
			return basDicInfoDao.getDicMxInfo(type);
		}else {
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> getUserDicZTree(Bridge bridge, String userpkid, String type) {
		List<Map<String, Object>> result = new ArrayList<>();
		//获取所有的树信息
		List<BasDicInfo> dics = basDicInfoDao.getChildrens(type);
		//获取用户信息
		UserInfo userInfo = userInfoDao.selectByuserid(userpkid);
		//获取该用户所拥有的字典信息，拼装为map
		Map<String,String> ownInfo = new HashMap<>();
		if(userInfo != null) {
			String[] postArray = userInfo.getPost().split(",");
			for(String post : postArray) {
				ownInfo.put(post,post);
			}
		}
		//循环拼凑所需的数据
		for (BasDicInfo info : dics) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", info.getDickey());
			map.put("pId", Global.NULLSTRING);
			map.put("name", info.getDickey() + " " + info.getDicval());
			//打勾选项
			if (CommonUtil.isNotEmpty(ownInfo.get(info.getDickey()))) {
				map.put("checked", true);
			}
			result.add(map);
		}
		return result;
	}

	@Override
	public Map<String, String> getDicKeyValue(Bridge bridge, String type) {
		//获取所有的树信息
		List<BasDicInfo> dics = basDicInfoDao.getChildrens(type);
		Map<String,String> result = new HashMap<>();
		for(BasDicInfo dic : dics) {
			result.put(dic.getDickey(),dic.getDicval());
		}
		return result;
	}
}