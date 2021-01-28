package com.main.service.platform.impl;

import com.common.CommonUtil;
import com.main.dao.platform.CommonDao;
import com.main.dao.platform.CompanyInfoDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.CompanyInfo;
import com.main.pojo.platform.StateInfo;
import com.main.service.platform.CompanyInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象服务层实现类
 *
 * @author Tim[ATC.Pro Generate]
 */
@Service
@Transactional
public class CompanyInfoServiceImpl implements CompanyInfoService {
	@Resource
	private CompanyInfoDao companyInfoDao;

	@Override
	public CompanyInfo selectBycompanyid(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.companyInfoDao.selectBycompanyid(map);
	}

	@Override
	public StateInfo validator(String key, String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (!CommonUtil.isEmpty(companyInfoDao.validator(key, value, bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), "已录入重复的companyid编号，请修改！", null);
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
		Map<String, Object> resultMap = this.companyInfoDao.getCount(map);
		return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
	}

	@Override
	public List<CompanyInfo> get(String keyword, String node, Bridge bridge, String start, String limit) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		map.put("start", start);
		map.put("end", Integer.parseInt(start) + Integer.parseInt(limit));
		return this.companyInfoDao.getPage(map);
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
				this.companyInfoDao.deleteByPrimaryKey(map);
				if (!CommonUtil.isEmpty(parentId) && !"0".equals(parentId) && !"root".equals(parentId)) {
					map.put("node", parentId);
					map.put("companyid", bridge.getCompanyid());

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
	private Map<String, Object> getBeanInfoMap(CompanyInfo companyInfo, Bridge bridge) {
		companyInfo.setCompanyid(bridge.getCompanyid());

		Map<String, Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("companyid", companyInfo.getCompanyid());
		map.put("companyname", companyInfo.getCompanyname());
		map.put("isstop", companyInfo.getIsstop());
		return map;
	}

	@Override
	public StateInfo edit(String companyid, CompanyInfo companyInfo, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(companyid) && companyInfo != null) {
			Map<String, Object> map = this.getBeanInfoMap(companyInfo, bridge);
			map.put("whereId", companyid);
			try {
				this.companyInfoDao.update(map);
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
	public StateInfo add(String parentId, CompanyInfo companyInfo, Bridge bridge) {
		//tree init
		//自动生成主键按1 101 10101规则
		Map<String, Object> mapOfID = this.companyInfoDao.getAutoGeneralID(parentId, bridge);
		companyInfo.setCompanyid(CommonUtil.dealPKRule(mapOfID, parentId));
		Map<String, Object> map = this.getBeanInfoMap(companyInfo, bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			this.companyInfoDao.insert(map);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

}