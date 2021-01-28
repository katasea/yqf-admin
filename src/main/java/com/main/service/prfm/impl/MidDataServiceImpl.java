package com.main.service.prfm.impl;

import com.common.*;
import com.common.constants.GlobalConstants;
import com.main.aop.LogAnnotation;
import com.main.dao.platform.CommonDao;
import com.main.dao.platform.DeptInfoDao;
import com.main.dao.platform.UserInfoDao;
import com.main.dao.prfm.MidDataDao;
import com.main.pojo.platform.DeptInfo;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.UserInfo;
import com.main.pojo.prfm.MidData;
import com.main.pojo.platform.Bridge;
import com.main.pojo.prfm.MidDataDic;
import com.main.service.platform.DeptInfoService;
import com.main.service.platform.UserInfoService;
import com.main.service.prfm.AuthCenterService;
import com.main.service.prfm.MidDataDicService;
import com.main.service.prfm.MidDataService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
public class MidDataServiceImpl implements MidDataService {
	@Resource
	private MidDataDao midDataDao;
	@Resource
	private CommonDao comDao;
	@Resource
	private MidDataDicService midDataDicService;
	@Resource
	private DeptInfoDao deptInfoDao;
	@Resource
	private UserInfoDao userInfoDao;
	@Resource
	private AuthCenterService authCenterService;
	@Resource
	private DeptInfoService deptInfoService;
	@Resource
	private UserInfoService userInfoService;

	@LogAnnotation(moduleName = "辅助字典服务", option = "查询")
	@Override
	public MidData selectByid(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.midDataDao.selectByid(map);
	}

	@LogAnnotation(moduleName = "辅助字典服务", option = "验证编号唯一")
	@Override
	public StateInfo validator(String key, String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (!CommonUtil.isEmpty(midDataDao.validator(key, value, bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), "已录入重复的id编号，请修改！", null);
			}
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@Override
	public int getCount(String keyword, String midBh, Bridge bridge, String deptorper) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("midBh", midBh);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		map.put("yearmonth", bridge.getYmstr());
		map.put("deptorper", deptorper);
		Map<String, Object> resultMap;
		if (String.valueOf(GlobalConstants.DEPT_OR_PERSON_D).equals(deptorper)) {
			resultMap = this.deptInfoDao.getMBCount(map);
		} else {
			resultMap = this.userInfoDao.getCount(map);
		}
		return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
	}

	@LogAnnotation(moduleName = "辅助字典服务", option = "查询列表数据")
	@Override
	public List<MidData> get(String keyword, String midBh, Bridge bridge, String start, String limit, String deptorper) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("midBh", CommonUtil.nullToStr(midBh));
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		map.put("start", start);
		map.put("yearmonth", bridge.getYmstr());
		map.put("month", bridge.getMonth());
		map.put("end", Integer.parseInt(start) + Integer.parseInt(limit));
		map.put("deptorper", deptorper);
		List<MidData> result;
		if (String.valueOf(GlobalConstants.DEPT_OR_PERSON_D).equals(deptorper)) {
			result = this.midDataDao.getDeptPage(map);
		} else {
			result = this.midDataDao.getPerPage(map);
		}
		//小数位处理
		Map<String,String> dicInfos = midDataDicService.getDecInfo(midBh, bridge.getCompanyid());

		for (MidData data : result) {
			data.setData(CommonUtil.strFormatScale(data.getData(), Integer.parseInt(CommonUtil.nullToZero(CommonUtil.getMV(dicInfos,data.getId())))));
		}
		return result;
	}

	@LogAnnotation(moduleName = "辅助字典服务", option = "删除")
	@Override
	public StateInfo delete(String midBh, String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(midBh)) {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("midBh", midBh);
				//顺序不能乱
				this.midDataDao.deleteByMidBh(map);
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
	private Map<String, Object> getBeanInfoMap(MidData midData, Bridge bridge) {
		midData.setCompanyid(bridge.getCompanyid());

		Map<String, Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("companyid", midData.getCompanyid());
		map.put("id", midData.getId());
		map.put("bh", midData.getBh());
		map.put("deptorper", midData.getDeptorper());
		map.put("month", midData.getMonth());
		map.put("data", midData.getData());
		map.put("process", midData.getProcess());
		return map;
	}

	@LogAnnotation(moduleName = "辅助字典服务", option = "修改")
	@Override
	public StateInfo edit(String id, MidData midData, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(id) && midData != null) {
			Map<String, Object> map = this.getBeanInfoMap(midData, bridge);
			map.put("whereId", id);
			try {
				this.midDataDao.update(map);
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

	@LogAnnotation(moduleName = "辅助字典服务", option = "新增")
	@Override
	public StateInfo add(String parentId, MidData midData, Bridge bridge) {
		//tree init
		//自动生成主键按1 101 10101规则
//		Map<String, Object> mapOfID = this.midDataDao.getAutoGeneralID(parentId, bridge);
//		midData.setId(CommonUtil.dealPKRule(mapOfID, parentId));
		Map<String, Object> map = this.getBeanInfoMap(midData, bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			if(CommonUtil.isNotEmpty(midData.getBh())) {
				this.midDataDao.insert(map);
			}else {
				throw new RuntimeException("部门或个人编号为空");
			}
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@Override
	public StateInfo insertBatch(String year, List<MidData> list) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (list.size() != 0) {
				this.midDataDao.insertBatch(year, list);
			}
		} catch (Exception e) {
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
			stateInfo.setFlag(false);
		}
		return stateInfo;
	}

	@Override
	public StateInfo getDynamicColumn(Bridge bridge, String chooseParentId, String deptOrper) {
		StateInfo stateInfo = new StateInfo();
		try {
			//1 获取辅助数据
			List<MidDataDic> mxMidDicList = midDataDicService.getByParentid(bridge, chooseParentId);
			//2 获取权限信息
			List<Map<String, Object>> authList = authCenterService.getAuthList(null, GlobalConstants.AUTHCODE_MIDDIC_INPUT, bridge, bridge.getUserid(), String.valueOf(0), String.valueOf(Global.TABLE_MAX_RECORD));
			Map<String, Boolean> authMap = new HashMap<>();
			for (Map<String, Object> map : authList) {
				authMap.put(String.valueOf(map.get("mtarg")), true);
			}
			List<MidDataDic> resultList = new ArrayList<>();
			for (MidDataDic dic : mxMidDicList) {
				//判断个人还是部门  0 部门 1 个人
				if(deptOrper.equals(String.valueOf(dic.getDeptorper()))) {
					//3 判断是否有权限
					//超级管理员啥都让看。
					if (bridge.isAdmin()) {
						resultList.add(dic);
					} else {
						if (authMap.containsKey(dic.getId())) {
							resultList.add(dic);
						}
					}
				}
			}
			stateInfo.setData(resultList);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@Override
	public Map<String, Object> getDynamicJson(Bridge bridge, String deptOrper, String chooseParentId, String start, String limit, String keyword) {
		//获取所有的数据
		int count = 0;
		List<Map<String,Object>> resultList = new ArrayList<>();

		//===================获取中间数据===========================//
		List<MidData> datas = this.get(keyword,null,bridge,"0",String.valueOf(Global.TABLE_MAX_RECORD),deptOrper);
		//bmbh,C+id,data
		Map<String,Map<String,String>> dataMaps = new HashMap<>();
		if(datas != null) {
			for(MidData data : datas) {
				Map<String,String> map = dataMaps.get(data.getBh());
				if(map == null) map=new HashMap<>();
				map.put('C'+data.getId(),data.getData());
				dataMaps.put(data.getBh(),map);
			}
		}
		//================拼凑中间数据结束==========================//

		if(String.valueOf(GlobalConstants.DEPT_OR_PERSON_D).equals(deptOrper)) {
			List<DeptInfo> deptInfoMBs = deptInfoService.getMB(keyword,bridge,start,limit,GlobalConstants.AUTHCODE_MIDDIC_INPUT_BM);
			count = deptInfoService.getMBCount(keyword,bridge,GlobalConstants.AUTHCODE_MIDDIC_INPUT_BM);
			for(DeptInfo dept : deptInfoMBs) {
				Map<String,Object> rowInfo = new HashMap<>();
				rowInfo.put("bh",dept.getDeptid());
				rowInfo.put("name",dept.getDeptname());
				Map<String,String> dataRow = CommonUtil.getMV(dataMaps,dept.getDeptid());
				if(dataRow!=null) {
					rowInfo.putAll(dataRow);
				}
				resultList.add(rowInfo);
			}
		}else if(String.valueOf(GlobalConstants.DEPT_OR_PERSON_P).equals(deptOrper)) {
			List<UserInfo> userInfos = userInfoService.getWithAuth(keyword,bridge,start,limit,GlobalConstants.AUTHCODE_MIDDIC_INPUT_BM);
			count = userInfoService.getCountWithAuth(keyword,bridge,GlobalConstants.AUTHCODE_MIDDIC_INPUT_BM);
			for(UserInfo user : userInfos) {
				Map<String,Object> rowInfo = new HashMap<>();
				rowInfo.put("bh",user.getUserid());
				rowInfo.put("name",user.getUsername());
				rowInfo.put("deptbh",user.getDeptname());
				Map<String,String> dataRow = CommonUtil.getMV(dataMaps,user.getUserid());
				if(dataRow!=null) {
					rowInfo.putAll(dataRow);
				}
				resultList.add(rowInfo);
			}
		}

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("totalSize", count);
		resultMap.put("root", resultList);
		return resultMap;
	}
}