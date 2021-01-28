package com.main.service.platform.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.AccessOptUtil;
import com.common.CommonUtil;
import com.common.ExportExcelUtil;
import com.common.Global;
import com.common.JsonUtil;
import com.common.ZjmUtil;
import com.main.dao.platform.CommonDao;
import com.main.dao.platform.DeptInfoDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.DeptInfo;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.result.DeptTreeInfo;
import com.main.service.platform.BaseDicInfoService;
import com.main.service.platform.DeptInfoService;
//import com.main.service.platform.QuartzServer;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;

/**
 * 对象服务层实现类
 *
 * @author Tim[ATC.Pro Generate]
 */
@Service
@Transactional
//public class DeptInfoServiceImpl implements DeptInfoService,QuartzServer{
public class DeptInfoServiceImpl implements DeptInfoService{
	@Resource
	private DeptInfoDao deptInfoDao;
	@Resource
	private CommonDao comDao;
	@Resource
	private BaseDicInfoService basDicService;

	@Override
	public DeptInfo selectByPkid(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.deptInfoDao.selectBypid(map);
	}

	@Override
	public StateInfo validator(String key, String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (!CommonUtil.isEmpty(deptInfoDao.validator(key, value, bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), "科室编号已存在请重新输入！", null);
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
		Map<String, Object> resultMap = this.deptInfoDao.getCount(map);
		return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
	}

	@Override
	public List<DeptInfo> get(String keyword, String node, Bridge bridge) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		List<DeptInfo> result = this.deptInfoDao.get(map);
		//获取岗位
		Map<String, String> deptTypeMap = basDicService.getDicKeyValue(bridge, "1");
		if (CommonUtil.isEmpty(keyword)) {
			for (DeptInfo m : result) {
				m.setId(String.valueOf(m.getPid()));
				m.setLeaf(m.getIsleaf() == 1);
				m.setDepttypeText(deptTypeMap.get(m.getDepttype()));
				m.setCompanyname(bridge.getCompanyname());
			}
		} else {
			for (DeptInfo m : result) {
				m.setId(String.valueOf(m.getPid()));
				m.setLeaf(true);
				m.setDepttypeText(deptTypeMap.get(m.getDepttype()));
				m.setCompanyname(bridge.getCompanyname());
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
				map.put("companyid", bridge.getCompanyid());
				map.put("id", code);
				map.put("yearmonth", bridge.getYmstr());

				this.deptInfoDao.deleteByPrimaryKey(map);
				if (!CommonUtil.isEmpty(parentId) && !"0".equals(parentId) && !"root".equals(parentId)) {
					map.put("node", parentId);
					map.put("companyid", bridge.getCompanyid());

					List<DeptInfo> childrens = this.deptInfoDao.get(map);
					//父节点没有子节点要把父节点的leaf 设置为0
					if (childrens == null || childrens.size() == 0) {
						//父节点leaf 设置为0
						this.deptInfoDao.changeLeaf(this.createLeafMap(bridge,parentId,"1"));
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
	private Map<String, Object> getBeanInfoMap(DeptInfo deptInfo, Bridge bridge) {
		deptInfo.setCompanyid(bridge.getCompanyid());

		Map<String, Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("pid", deptInfo.getPid());
		map.put("companyid", deptInfo.getCompanyid());
		map.put("deptid", deptInfo.getDeptid());
		map.put("deptname", deptInfo.getDeptname());
		map.put("zjm", deptInfo.getZjm());
		map.put("depttype", deptInfo.getDepttype());
		map.put("parentid", deptInfo.getParentid());
		map.put("isleaf", deptInfo.getIsleaf());
		map.put("isstop", deptInfo.getIsstop());
		return map;
	}

	@Override
	public StateInfo edit(String pid, DeptInfo deptInfo, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(pid) && deptInfo != null) {
			//判断历史记录与当前记录
			DeptInfo oldInfo = this.selectByPkid(pid);
			if (oldInfo.getIsstop() == deptInfo.getIsstop()) {
				//停用记录与原先一直，保持原先的停用时间。
				deptInfo.setStoptime(oldInfo.getStoptime());
			} else {
				//原来是停用，现在启用了，也要记录原来停用的时间
				if (oldInfo.getIsstop() == 1) {
					deptInfo.setStoptime(oldInfo.getStoptime());
				}
			}
			Map<String, Object> map = this.getBeanInfoMap(deptInfo, bridge);
			map.put("whereId", pid);
			map.put("yearmonth",bridge.getYmstr());
			try {
				this.deptInfoDao.update(map);
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
	public StateInfo add(String parentId, DeptInfo deptInfo, Bridge bridge) {
		deptInfo.setIsleaf(1);
		if (!CommonUtil.isEmpty(parentId)) {
			deptInfo.setParentid(parentId);
		} else {
			deptInfo.setParentid(Global.NULLSTRING);
		}
		deptInfo.setZjm(ZjmUtil.generateZJM(deptInfo.getDeptname()));

		//自动生成主键按1 101 10101规则
		Map<String, Object> mapOfID = this.deptInfoDao.getAutoGeneralID(parentId, bridge);
		deptInfo.setPid(CommonUtil.dealPKRule(mapOfID, parentId));
//		deptInfo.setDeptid(deptInfo.getPid());
		Map<String, Object> map = this.getBeanInfoMap(deptInfo, bridge);
		map.put("yearmonth",bridge.getYmstr());
		StateInfo stateInfo = new StateInfo();
		try {
			if (!CommonUtil.isEmpty(parentId)) {
				//父节点leaf 设置为0
				this.deptInfoDao.changeLeaf(this.createLeafMap(bridge,parentId,"0"));
			}
			this.deptInfoDao.insert(map);
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
	public StateInfo autoInitFromAccess(Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = AccessOptUtil.connectAccessFile();
			if (conn != null) {
				this.deptInfoDao.deleteAll(bridge.getYear(),bridge.getYmstr());
				stmt = conn.createStatement();
				rs = stmt.executeQuery("Select * From PM_DEPT");
				List<String> sqlList = new ArrayList<>();
				StringBuilder sqlBuffer = new StringBuilder();
				while (rs.next()) {
					sqlBuffer.setLength(0);
					sqlBuffer.append("Insert Into PM_DEPT");
					sqlBuffer.append("(pid,companyid,deptid,deptname,zjm,depttype,parentid,isleaf,isstop,stoptime,inserttime) values (");
					sqlBuffer.append("'").append(rs.getString("deptid")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(bridge.getCompanyid()).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("deptid")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("deptname")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("zjm")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("depttype")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(CommonUtil.nullToStr(rs.getString("parentid"))).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("isleaf")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'0'");
					sqlBuffer.append(",'',''");
					sqlBuffer.append(")");
					sqlList.add(sqlBuffer.toString());
				}
				comDao.transactionUpdate(sqlList);
				//备份当月数据
				deptInfoDao.backupMonthData(bridge.getYmstr());
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
	public StateInfo importData(File targetFile, Bridge bridge) {
		HSSFWorkbook wb;
		StateInfo stateInfo = new StateInfo();
		try {
			//1. 获取已有的所有账号，防止重复添加。
			List<DeptInfo> existDeptList = deptInfoDao.getAll(bridge.getCompanyid());
			Map<String,String> existDept = new HashMap<>();
			for(DeptInfo dept: existDeptList) {
				existDept.put(dept.getDeptid(),dept.getDeptname());
			}
			//2. 获取已有的字典，防止字典输入错误。
			//获取科室类型
			Map<String, String> deptTypeMap = basDicService.getDicKeyValue(bridge, "1");
			List<DeptInfo> leafs = deptInfoDao.getLeafs(bridge.getCompanyid());
			Map<String,String> leafsDB = new HashMap<>();
			for(DeptInfo dept : leafs) {
				leafsDB.put(dept.getDeptid(),"1");
			}
			//开始解析excel数据
			List<DeptInfo> deptInfos = new ArrayList<>();
			wb = new HSSFWorkbook(new FileInputStream(targetFile));
			HSSFSheet sheet = wb.getSheetAt(0);
			int beginIndex = 1; // 数据的起始行
			int rows = sheet.getLastRowNum();
			int j;

			//先读取一遍获取所需的东西
			Map<String,String> deptidImportMap = new HashMap<>();
			Map<String,String> parentidImportMap = new HashMap<>();

			for (int i = beginIndex; i <= rows; i++) {
				deptidImportMap.put(CommonUtil.getCellValue(sheet.getRow(i).getCell(0)),"1");
				parentidImportMap.put(CommonUtil.getCellValue(sheet.getRow(i).getCell(3)),"1");
			}

			for (int i = beginIndex; i <= rows; i++) {
				j = 0;
				DeptInfo deptInfo = new DeptInfo();
				deptInfo.setDeptid(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if(CommonUtil.isEmpty(deptInfo.getDeptid())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(),"第"+(i+1)+"行，第"+j+"列，科室编号为空，请校对编辑后重新上传",null);
					return stateInfo;
				}
				if(CommonUtil.isNotEmpty(existDept.get(deptInfo.getDeptid()))){
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(),"第"+(i+1)+"行，第"+j+"列，科室编号已存在，请校对编辑后重新上传",null);
					return stateInfo;
				}

				deptInfo.setDeptname(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if(CommonUtil.isEmpty(deptInfo.getDeptname())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(),"第"+(i+1)+"行，第"+j+"列，科室名称为空，请校对编辑后重新上传",null);
					return stateInfo;
				}
				deptInfo.setZjm(ZjmUtil.generateZJM(deptInfo.getDeptname()));
				deptInfo.setIsstop(0);
				deptInfo.setCompanyid(bridge.getCompanyid());
				deptInfo.setStoptime(Global.NULLSTRING);
				deptInfo.setInserttime(Global.NULLSTRING);
				deptInfo.setPid(deptInfo.getDeptid());

				deptInfo.setDepttype(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if(CommonUtil.isEmpty(deptInfo.getDepttype())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(),"第"+(i+1)+"行，第"+j+"列，用户编号为空，请校对编辑后重新上传",null);
					return stateInfo;
				}
				if(CommonUtil.isEmpty(deptTypeMap.get(deptInfo.getDepttype()))) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(),"第"+(i+1)+"行，第"+j+"列，部门类型编号未存在，请校对编辑后重新上传",null);
					return stateInfo;
				}
				deptInfo.setParentid(CommonUtil.nullToStr(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++))));
				if(CommonUtil.isNotEmpty(deptInfo.getParentid())&&CommonUtil.isEmpty(existDept.get(deptInfo.getParentid()))&&CommonUtil.isEmpty(deptidImportMap.get(deptInfo.getParentid()))) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(),"第"+(i+1)+"行，第"+j+"列，父节点编号未存在，请校对编辑后重新上传",null);
					return stateInfo;
				}
				//如果在父节点列表没有找到，说明他是子节点，否则为父节点
				if(CommonUtil.isEmpty(parentidImportMap.get(deptInfo.getDeptid()))) {
					deptInfo.setIsleaf(1);
				}else {
					deptInfo.setIsleaf(0);
				}
				deptInfos.add(deptInfo);
			}
			//save depts
			deptInfoDao.insertDeptInfos(deptInfos);
			deptInfoDao.backupMonthData(bridge.getYmstr());
			//以前是子节点，后面导入后变成父节点的那些数据要改变他的isleaf
			for(String deptId : leafsDB.keySet()) {
				if(CommonUtil.isNotEmpty(parentidImportMap.get(deptId))) {
					this.deptInfoDao.changeLeaf(this.createLeafMap(bridge,deptId,"0"));
				}
			}

		}catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}finally {
			targetFile.delete();
			targetFile.deleteOnExit();
		}
		return stateInfo;
	}

	private Map<String,Object> createLeafMap(Bridge bridge, String deptId, String value) {
		Map<String, Object> map = new HashMap<>();
		map.put("companyid",bridge.getCompanyid());
		map.put("id", deptId);
		map.put("value", value);
		map.put("yearmonth", bridge.getYmstr());
		return map;
	}

	@Override
	public StateInfo exportExcelTemplate(OutputStream os, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		String sheetName = "科室信息";   //导出的Excel名称
		String [] headers = {"*科室编号","*科室名称","*科室类型编码","*归属编号[父节点]"};
		String [] columns = {"deptid","deptname","depttype","parentid"};
		List<DeptInfo> deptInfos = new ArrayList<>();
		DeptInfo deptInfo = new DeptInfo();
		deptInfos.add(deptInfo);
		ExportExcelUtil<DeptInfo> util = new ExportExcelUtil<>();
		try {
			util.export(sheetName,headers,columns,deptInfos,os);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	@Override
	public StateInfo exportData(OutputStream os,Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		String sheetName = "科室信息";   //导出的Excel名称
		String [] headers = {"科室编号","科室名称","科室类型","归属编号","状态[0启用1停用]","归属单位","停用时间","启用时间"};
		String [] columns = {"deptid","deptname","depttype","parentid","isstop","companyname","stoptime","inserttime"};
		List<DeptInfo> deptInfos = deptInfoDao.getAll(bridge.getCompanyid());
		//获取岗位
		Map<String, String> deptTypeMap = basDicService.getDicKeyValue(bridge, "1");
		for (DeptInfo m : deptInfos) {
			m.setDepttype(deptTypeMap.get(m.getDepttype()));
			m.setCompanyname(bridge.getCompanyname());
		}
		ExportExcelUtil<DeptInfo> util = new ExportExcelUtil<>();
		try {
			util.export(sheetName,headers,columns,deptInfos,os);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	@Override
	public void backupMonthData(String year_month) {
		deptInfoDao.backupMonthData(year_month);
	}
	

    public List<Map<String,Object>> getDeptTree(Bridge bridge){
    	List<Map<String,Object>> resultList =new ArrayList<Map<String,Object>>();
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("companyid", bridge.getCompanyid());
    	List<DeptInfo> top = deptInfoDao.get(map);
    	List<DeptTreeInfo> deptInfoList = new ArrayList<DeptTreeInfo>();
        for (DeptInfo subItem : top) {
             DeptTreeInfo subInfo = getAllDepartInfo(subItem,bridge);
             deptInfoList.add(subInfo);
             Map<String,Object> topMap = new HashMap<String,Object>();
             topMap.put("id",subItem.getPid());
             topMap.put("text",subItem.getDeptname());
             topMap.put("leaf",subItem.getIsleaf());
             topMap.put("children", subInfo.getChildrenList());
        	 resultList.add(topMap);
        }
    	return resultList;
    }

	@Override
	public List<DeptInfo> getListJson(String keyword, String node, Bridge bridge, String start, String limit) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
		map.put("start", start);
		map.put("end", Integer.parseInt(start)+Integer.parseInt(limit));
		return this.deptInfoDao.getPage(map);
	}
	@Override
	public List<DeptInfo> getMB(String keyword, Bridge bridge, String start, String limit, String authcode) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
		map.put("start", start);
		map.put("yearmonth", bridge.getYmstr());
		map.put("userid",bridge.getUserid());
		map.put("authcode", authcode);
		map.put("end", Integer.parseInt(start)+Integer.parseInt(limit));
		return this.deptInfoDao.getMBPage(map);
	}

	@Override
	public int getMBCount(String keyword, Bridge bridge, String authcode) {
		Map<String,Object> paramMap = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		paramMap.put("keyword", keyword);
		paramMap.put("year", bridge.getYear());
		paramMap.put("companyid",bridge.getCompanyid());
		paramMap.put("yearmonth", bridge.getYmstr());
		paramMap.put("userid",bridge.getUserid());
		paramMap.put("authcode", authcode);
		Map<String,Object> resultMap = deptInfoDao.getMBCount(paramMap);
		return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
	}


	public DeptTreeInfo getAllDepartInfo( DeptInfo deptInfo,Bridge bridge) {
    	DeptTreeInfo result = new DeptTreeInfo();
        result.setDeptInfo(deptInfo);
        DeptInfo query = new DeptInfo();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("node", deptInfo.getPid());
        map.put("isstop",1);
        map.put("companyid", bridge.getCompanyid());
        List<DeptInfo> subList = deptInfoDao.get(map);
        if (subList.size() > 0) {
            List<DeptInfo> subResultList = new ArrayList<DeptInfo>();
            List<Map<String,Object>> childrenList = new ArrayList<>();
            for (DeptInfo item : subList) {
                DeptTreeInfo subInfo = getAllDepartInfo(item,bridge);
            	Map<String,Object>  children = new HashMap<String,Object>();
            	children.put("id",subInfo.getDeptInfo().getPid());
            	children.put("text",subInfo.getDeptInfo().getDeptname());
            	children.put("leaf",subInfo.getDeptInfo().getIsleaf());
            	children.put("children", subInfo.getChildrenList());
            	childrenList.add(children);
            }
            result.setSub(subResultList);
            result.setChildrenList(childrenList);
        }
        return result;

    }
    
    

//	@Override
//	public void execute(JobExecutionContext context) throws JobExecutionException {
//		Logger.getLogger(this.getClass()).info("Hello DEPT执行时间: " + new Date());
//	}
}