package com.main.service.prfm.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.common.AccessOptUtil;
import com.common.CommonUtil;
import com.common.ExportExcelUtil;
import com.common.Global;
import com.common.JsonUtil;
import com.common.excel.ParseExcelUtil;
import com.main.aop.LogAnnotation;
import com.main.dao.platform.CommonDao;
import com.main.dao.prfm.NormDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.DeptInfo;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.Norm;
import com.main.pojo.result.DeptTreeInfo;
import com.main.pojo.result.NormTree;
import com.main.service.prfm.NormService;

/**
 * 对象服务层实现类
 * @author Tim[ATC.Pro Generate]
 */
@Service
@Transactional
public class NormServiceImpl implements NormService {
	@Resource
	private NormDao normDao;
	@Resource
	private CommonDao comDao;
	@Resource
	private ParseExcelUtil parseExcelUtil;
    @LogAnnotation(moduleName = "质量综合指标", option = "查询")
	@Override
	public Norm selectByuid(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.normDao.selectByuid(map);
	}

	@LogAnnotation(moduleName = "质量综合指标", option = "验证编号唯一")
	@Override
	public StateInfo validator(String key,String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if(!CommonUtil.isEmpty(normDao.validator(key,value,bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(),"已录入重复的uid编号，请修改！",null);
			}
		}catch(Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	@Override
    public int getCount(String keyword,String node,Bridge bridge) {
        Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
        Map<String,Object> resultMap = this.normDao.getCount(map);
        return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
    }

	@LogAnnotation(moduleName = "质量综合指标", option = "查询列表数据")
	@Override
	public List<Norm> get(String keyword, String node, Bridge bridge, String start, String limit,String deptOrPerson) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
		map.put("deptOrPerson", deptOrPerson);
		List<Norm> result = this.normDao.get(map);
		if(CommonUtil.isEmpty(keyword)) {
			for(Norm m : result) {
				m.setId(String.valueOf(m.getUid()));
				m.setLeaf(m.getIsleaf()==1);
			}
		}else {
			for(Norm m : result) {
				m.setId(String.valueOf(m.getUid()));
				m.setLeaf(true);
			}
		}
		return result;
	}
	
	@LogAnnotation(moduleName = "质量综合指标", option = "查询列表数据")
	@Override
	public List<Norm> getByCondition(String keyword, String node, Bridge bridge, String start, String limit) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
		List<Norm> result = this.normDao.getByCondition(map);
		if(CommonUtil.isEmpty(keyword)) {
			for(Norm m : result) {
				m.setId(String.valueOf(m.getUid()));
				m.setLeaf(m.getIsleaf()==1);
			}
		}else {
			for(Norm m : result) {
				m.setId(String.valueOf(m.getUid()));
				m.setLeaf(true);
			}
		}
		return result;
	}
	
	

    @LogAnnotation(moduleName = "质量综合指标", option = "删除")
	@Override
	public StateInfo delete(String code,String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(code)) {
			try {
				Map<String,Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id",code);
				//顺序不能乱
				this.normDao.deleteByPrimaryKey(map);
				if(!CommonUtil.isEmpty(parentId) && !"0".equals(parentId) && !"root".equals(parentId)) {
					map.put("node",parentId);
					map.put("companyid",bridge.getCompanyid());

					List<Norm> childrens = this.normDao.get(map);
					//父节点没有子节点要把父节点的leaf 设置为0
					if(childrens == null || childrens.size() == 0) {
						Map<String,Object> map2 = new HashMap<>();
						map2.put("year", bridge.getYear());
						map2.put("id",parentId);
						map2.put("value","1");
						//父节点leaf 设置为0
						this.normDao.changeLeaf(map2);
					}
				}
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
	private Map<String,Object> getBeanInfoMap(Norm norm, Bridge bridge){
		norm.setCompanyid(bridge.getCompanyid());
		
		Map<String,Object> map = new HashMap<>();
		map.put("companyid", bridge.getCompanyid());
		map.put("uid",norm.getUid());
		map.put("companyid",norm.getCompanyid());
		map.put("text",norm.getText());
		map.put("checknorm",norm.getChecknorm());
		map.put("recordnorm",norm.getRecordnorm());
		map.put("kouFenType",norm.getKouFenType());
		map.put("zhiBiaoType",norm.getZhiBiaoType());
		map.put("jiXiaoZhanBi",norm.getJiXiaoZhanBi());
		map.put("formula",norm.getFormula());
		map.put("bigtype",norm.getBigtype());
		map.put("orderid",norm.getOrderid());
		map.put("iscomp",norm.getIscomp());
		map.put("isstop",norm.getIsstop());
		map.put("score",norm.getScore());
		map.put("rec1",norm.getRec1());
		map.put("recsum",norm.getRecsum());
		map.put("recforce",norm.getRecforce());
		map.put("parentid",norm.getParentid());
		map.put("isleaf",norm.getIsleaf());
		map.put("year",bridge.getYear());
		return map;
	}

    @LogAnnotation(moduleName = "质量综合指标", option = "修改")
	@Override
	public StateInfo edit(String uid, Norm norm, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(uid) && norm != null) {
			Map<String,Object> map = this.getBeanInfoMap(norm,bridge);
			map.put("whereId",uid);
			try {
				this.normDao.update(map);
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

    @LogAnnotation(moduleName = "质量综合指标", option = "新增")
	@Override
	public StateInfo add(String parentId,Norm norm, Bridge bridge) {
		//tree init
        norm.setIsleaf(1);
		if(!CommonUtil.isEmpty(parentId)) {
                norm.setParentid(parentId);
		}else {
                norm.setParentid(Global.NULLSTRING);
		}
		//自动生成主键按1 101 10101规则
		Map<String,Object> mapOfID = this.normDao.getAutoGeneralID(parentId,bridge);
		norm.setUid(dealPKRule(mapOfID,parentId));
		Map<String,Object> map = this.getBeanInfoMap(norm,bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			if(!CommonUtil.isEmpty(parentId)) {
				Map<String,Object> map2 = new HashMap<>();
				map2.put("year", bridge.getYear());
				map2.put("id",parentId);
				map2.put("value","0");
				//父节点leaf 设置为0
				this.normDao.changeLeaf(map2);
			}
			this.normDao.insert(map);
		}catch(Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	/**
	 *初始化内置数据
	 */
     @LogAnnotation(moduleName = "质量综合指标", option = "初始化数据")
	 @Override
	 public StateInfo autoInitFromAccess(Bridge bridge){
	 	StateInfo stateInfo = new StateInfo();
	 	Connection conn = null;
	 	Statement stmt = null;
	 	ResultSet rs = null;
	 	try{
			this.normDao.deleteAll(bridge.getYear());
			conn = AccessOptUtil.connectAccessFile();
            if(conn != null) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("Select * From PM_NORM");
                List<String> sqlList = new ArrayList<>();
                StringBuilder sqlBuffer = new StringBuilder();
                while(rs.next()) {
                    sqlBuffer.setLength(0);
                    sqlBuffer.append("Insert Into PM_NORM");
                    sqlBuffer.append("(uid,companyid,text,checknorm,recordnorm,kouFenType,zhiBiaoType,jiXiaoZhanBi,formula,bigtype,orderid,iscomp,isstop,score,rec1,recsum,recforce,parentid,isleaf,year) values (");
                    sqlBuffer.append("'").append(rs.getString("uid")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(bridge.getCompanyid()).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("text")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("checknorm")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("recordnorm")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("kouFenType"));
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("zhiBiaoType"));
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getDouble("jiXiaoZhanBi"));
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("formula")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("bigtype"));
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("orderid"));
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("iscomp"));
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("isstop"));
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getDouble("score"));
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getDouble("rec1"));
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getDouble("recsum"));
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("recforce"));
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("parentid")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("isleaf")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("year"));
                
                    sqlBuffer.append(")");
                    sqlList.add(sqlBuffer.toString());
                }
                comDao.transactionUpdate(sqlList);
            }
		}catch(Exception e){
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}finally {
			Global.close(conn,stmt,rs);
		}
	 	return stateInfo;
	 }
     
     
     @LogAnnotation(moduleName = "质量综合指标", option = "导入EXCEL质量综合指标信息")
 	@Override
 	public StateInfo importData(File targetFile, Bridge bridge) throws FileNotFoundException {

  		StateInfo stateInfo = new StateInfo();
//    	File xmlFile = new File(System.getProperty("user.dir") + Global.FILE_UPLOADTPL + "/zongHeZhiLiangZhiBiao.xml");
//    	InputStream inputStream=this.getClass().getResourceAsStream("/xmlTemplate/zongHeZhiLiangZhiBiao.xml");

		  File xmlFile = ResourceUtils.getFile("classpath:xmlTemplate/zongHeZhiLiangZhiBiao.xml");
    	final ParseExcelUtil excleUtil = parseExcelUtil.init(targetFile, xmlFile,bridge.getUsername());
    	if(excleUtil.getErrorLineCount()>0){
    		stateInfo.setFlag(false);
			//stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，邮箱为空，请校对编辑后重新上传", null);
    		stateInfo.setMsg(this.getClass(), excleUtil.getErrorString().toString(), null);
			return stateInfo;
    	}
		List<Norm> list = new ArrayList<Norm>();
		List<Norm> errorList = new ArrayList<Norm>();//存放重复值的数据
		Map seqMap = excleUtil.getParseXmlUtil().getSeqMap();
        for (int i = 0; i < excleUtil.getListDatas().size(); i++) {
        	System.out.println("开始执行插入数据:"+i);
            Map excelCol = (Map) excleUtil.getListDatas().get(i); // 得到第
            // i
            // 行的数据
            Norm norm = new Norm();
            @SuppressWarnings("unchecked")
            Set<String> keyset = excelCol.keySet();
            int seq=0;
            for (Iterator it = keyset.iterator(); it.hasNext();) {// 遍历map
                String key = (String) it.next();
                int index = key.indexOf(".");
                String left = key.substring(0, index);
                String right = key.substring(index + 1, key.length());
                if ("t1".equals(left) && null != excelCol.get(key)) {
                	String value = excelCol.get(key).toString();
                	switch (Integer.parseInt((String)seqMap.get(right))){
					case 0:norm.setUid(value);break;
					case 1:norm.setText(value);break;
					case 2:norm.setParentid(value==null?"":value);break;
                	case 3:norm.setChecknorm(value);break;
                	case 4:norm.setRecordnorm(value);break;
                	case 5:norm.setKouFenType(Integer.parseInt(value));break;
                	case 6:norm.setZhiBiaoType(Integer.parseInt(value));break;
                	case 7:norm.setJiXiaoZhanBi(Double.valueOf(value));break;
                	case 8:norm.setFormula(value);break;
                	case 9:norm.setIscomp(Integer.parseInt(value));break;
                	case 10:norm.setIsstop(Integer.parseInt(value));break;
                	case 11:norm.setScore(Double.valueOf(value));break;
                	case 12:norm.setRec1(Double.valueOf(value));break;
                	case 13:norm.setRecsum(Double.valueOf(value));break;
                	case 14:norm.setRecforce(Integer.valueOf(value));break;
                	case 15:norm.setYear(Integer.parseInt(value));break;
                	case 16:norm.setBigtype(Integer.parseInt(value));break;
                	case 17:norm.setIsleaf(Integer.parseInt(value));break;
                	} 
                }
            }
            if(norm.getParentid()==null){
            	norm.setParentid("");
            }
            if(norm.getYear()==0){
            	norm.setYear(Integer.parseInt(bridge.getYear()));
            }
            norm.setCompanyid(bridge.getCompanyid());
            list.add(norm);
        }
    	

		if(list.size()>0){
		   normDao.insertBatch(list);
		   for(Norm norm:list){
			   if(!CommonUtil.isEmpty(norm.getParentid())) {
					Map<String,Object> map2 = new HashMap<>();
					map2.put("id",norm.getParentid());
					map2.put("value","0");
					//父节点leaf 设置为0
					this.normDao.changeLeaf(map2);
			   }
		   }
		}
 		return stateInfo;
 	}

 	@LogAnnotation(moduleName = "质量综合指标", option = "导出用户模板")
 	@Override
 	public StateInfo exportExcelTemplate(OutputStream os, Bridge bridge) {
 		StateInfo stateInfo = new StateInfo();
 		String sheetName = "质量综合指标信息";   //导出的Excel名称
 		String[] headers = {"uid*","指标名称*","父id", "考核标准", "评分标准", "扣分类型", "指标类型", "绩效占比",
 				"公式", "是否计分", "是否停用", "标准分数", "单次扣分", "最多扣分", "扣分是否可改","年份","部门/个人","是否叶子节点"};
 		String[] columns = {"uid", "text","parentid", "checknorm", "recordnorm",
 				"kouFenType", "zhiBiaoType", "jiXiaoZhanBi", "formula", "iscomp", "isstop", "score", "rec1", "recsum","recforce","year","bigtype","isleaf"};
 		List<Norm> norms = new ArrayList<>();
 		Norm norm = new Norm();
 		norm.setUid("1302");
 		norm.setText("模板指标");
 		norm.setParentid("13");
 		norm.setChecknorm("考核标准");
 		norm.setRecordnorm("评分标准");
 		norm.setKouFenType(1);
 		norm.setZhiBiaoType(0);
 		norm.setJiXiaoZhanBi(22);
 		norm.setFormula("A*B");
 		norm.setIscomp(1);
 		norm.setIsstop(0);
 		norm.setScore(33);
 		norm.setRec1(2);
 		norm.setRecsum(20);
 		norm.setRecforce(1);
 		norm.setYear(2018);
 		norm.setBigtype(1);
 		norm.setIsleaf(1);
 		norms.add(norm);
 		
 		Norm norm1 = new Norm();
 		norm1.setUid("14");
 		norm1.setText("模板指标01");
 		norm1.setChecknorm("考核标准");
 		norm1.setRecordnorm("评分标准");
 		norm1.setKouFenType(1);
 		norm1.setZhiBiaoType(1);
 		norm1.setJiXiaoZhanBi(22);
 		norm1.setFormula("A*B");
 		norm1.setIscomp(1);
 		norm1.setIsstop(0);
 		norm1.setScore(33);
 		norm1.setRec1(2);
 		norm1.setRecsum(20);
 		norm1.setRecforce(50);
 		norm1.setYear(2018);
 		norm1.setBigtype(1);
 		norm1.setIsleaf(1);
 		norms.add(norm1); 	
 		ExportExcelUtil<Norm> util = new ExportExcelUtil<>();
 		try {
 			util.export(sheetName, headers, columns, norms, os);
 		} catch (Exception e) {
 			stateInfo.setFlag(false);
 			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
 		}
 		return stateInfo;
 	}

 	@LogAnnotation(moduleName = "质量综合指标", option = "导出质量综合指标EXCEL数据")
 	@Override
 	public StateInfo exportData(OutputStream os, Bridge bridge,String keyword) {
 		StateInfo stateInfo = new StateInfo();
 		String sheetName = "质量综合指标信息";   //导出的Excel名称
 		String[] headers = {"uid*","指标名称*","父id", "考核标准", "评分标准", "扣分类型", "指标类型", "绩效占比",
 				"公式", "是否计分", "是否停用", "标准分数", "单次扣分", "最多扣分", "扣分是否可改", "年份","部门/个人","是否叶子节点"};
 		String[] columns = {"uid", "parentid", "text", "checknorm", "recordnorm",
 				"kouFenType", "zhiBiaoType", "jiXiaoZhanBi", "formula", "iscomp", "isstop", "score", "rec1", "recsum","recforce","year","bigtype","isleaf"};
 		List<Norm> norms = this.getByCondition(keyword,null, bridge, "0", "999999999");
 		ExportExcelUtil<Norm> util = new ExportExcelUtil<>();
 		try {
 			util.export(sheetName, headers, columns, norms, os);
 		} catch (Exception e) {
 			stateInfo.setFlag(false);
 			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
 		}
 		return stateInfo;
 	}
 	
 	
 	
	/**
	 * 处理主键规则
	 *
	 * @param mapOfID  主键集合
	 * @param parentId 父节点
	 * @return 字符串主键
	 */
	private  String dealPKRule(Map<String, Object> mapOfID, String parentId) {
		String result;
		if (mapOfID == null) {
			if (CommonUtil.isEmpty(parentId)) {
				result = "01";
			} else {
				result = parentId + "01";
			}
		} else {
			String id = String.valueOf(mapOfID.get("id"));
			if(id.startsWith("0")){
				id = id.substring(1,id.length());
			}
			result = String.valueOf(Integer.parseInt(id) + 1);
            if(result.length()%2!=0){//不是2的倍数的补零
            	result = "0"+result;
            }
		}
		return result;
	}
	
	
    public List<Map<String,Object>> getDeptTree(Bridge bridge,String deptOrPerson,String zhiBiaoType){
    	List<Map<String,Object>> resultList =new ArrayList<Map<String,Object>>();
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("companyid", bridge.getCompanyid());
    	map.put("deptOrPerson", deptOrPerson);
    	map.put("zhiBiaoType", zhiBiaoType);
    	List<Norm> top = normDao.get(map);
    	List<NormTree> deptInfoList = new ArrayList<NormTree>();
        for (Norm subItem : top) {
             NormTree subInfo = getAllDepartInfo(subItem,bridge,deptOrPerson);
             deptInfoList.add(subInfo);
             Map<String,Object> topMap = new HashMap<String,Object>();
             topMap.put("id",subInfo.getNorm().getUid());
             topMap.put("text",subInfo.getNorm().getText());
             topMap.put("leaf",subInfo.getNorm().getIsleaf());
             topMap.put("children", subInfo.getChildrenList());
        	 resultList.add(topMap);
        }
        System.out.println(JsonUtil.getShowJson(resultList));
    	return resultList;
    }
    

    public NormTree getAllDepartInfo( Norm norm,Bridge bridge,String deptOrPerson) {
    	NormTree result = new NormTree();
        result.setNorm(norm);
        DeptInfo query = new DeptInfo();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("node", norm.getUid());
        map.put("isstop",1);
        map.put("companyid", bridge.getCompanyid());
        map.put("deptOrPerson", deptOrPerson);
        List<Norm> subList = normDao.get(map);
        if (subList.size() > 0) {
            List<Norm> subResultList = new ArrayList<Norm>();
            List<Map<String,Object>> childrenList = new ArrayList<>();
            for (Norm item : subList) {
                NormTree subInfo = getAllDepartInfo(item,bridge,deptOrPerson);
            	Map<String,Object>  children = new HashMap<String,Object>();
            	children.put("id",subInfo.getNorm().getUid());
            	children.put("text",subInfo.getNorm().getText());
            	children.put("leaf",subInfo.getNorm().getIsleaf());
            	children.put("children", subInfo.getChildrenList());
            	childrenList.add(children);
            }
            result.setSub(subResultList);
            result.setChildrenList(childrenList);
        }
        return result;

    }
}