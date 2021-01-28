package com.main.service.prfm.impl;

import com.common.*;
import com.main.aop.LogAnnotation;
import com.main.dao.platform.CommonDao;
import com.main.dao.prfm.ItemTypeDao;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.ItemType;
import com.main.pojo.platform.Bridge;
import com.main.service.prfm.ItemTypeService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

/**
 * 对象服务层实现类
 * @author Tim[ATC.Pro Generate]
 */
@Service
@Transactional
public class ItemTypeServiceImpl implements ItemTypeService {
	@Resource
	private ItemTypeDao itemTypeDao;
	@Resource
	private CommonDao comDao;

    @LogAnnotation(moduleName = "收支项目字典", option = "查询")
	@Override
	public ItemType selectBypid(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.itemTypeDao.selectBypid(map);
	}

	@LogAnnotation(moduleName = "收支项目字典", option = "验证编号唯一")
	@Override
	public StateInfo validator(String key,String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if(!CommonUtil.isEmpty(itemTypeDao.validator(key,value,bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(),"已录入重复的pid编号，请修改！",null);
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
        Map<String,Object> resultMap = this.itemTypeDao.getCount(map);
        return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
    }

	@LogAnnotation(moduleName = "收支项目字典", option = "查询列表数据")
	@Override
	public List<ItemType> get(String keyword, String node, Bridge bridge, String start, String limit) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
		List<ItemType> result = this.itemTypeDao.get(map);
		if(CommonUtil.isEmpty(keyword)) {
			for(ItemType m : result) {
				m.setId(String.valueOf(m.getPid()));
				m.setLeaf(m.getIsleaf()==1);
			}
		}else {
			for(ItemType m : result) {
				m.setId(String.valueOf(m.getPid()));
				m.setLeaf(true);
			}
		}
		return result;
	}

    @LogAnnotation(moduleName = "收支项目字典", option = "删除")
	@Override
	public StateInfo delete(String code,String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(code)) {
			try {
				Map<String,Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id",code);
				//顺序不能乱
				this.itemTypeDao.deleteByPrimaryKey(map);
				if(!CommonUtil.isEmpty(parentId) && !"0".equals(parentId) && !"root".equals(parentId)) {
					map.put("node",parentId);
					map.put("companyid",bridge.getCompanyid());

					List<ItemType> childrens = this.itemTypeDao.get(map);
					//父节点没有子节点要把父节点的leaf 设置为0
					if(childrens == null || childrens.size() == 0) {
						Map<String,Object> map2 = new HashMap<>();
						map2.put("year", bridge.getYear());
						map2.put("id",parentId);
						map2.put("value","1");
						//父节点leaf 设置为0
						this.itemTypeDao.changeLeaf(map2);
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
	private Map<String,Object> getBeanInfoMap(ItemType itemType, Bridge bridge){
		itemType.setCompanyid(bridge.getCompanyid());
		
		Map<String,Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("pid",itemType.getPid());
		map.put("companyid",itemType.getCompanyid());
		map.put("code",itemType.getCode());
		map.put("text",itemType.getText());
		map.put("itemtype",itemType.getItemtype());
		map.put("parentid",itemType.getParentid());
		map.put("isleaf",itemType.getIsleaf());
		map.put("isstop",itemType.getIsstop());
		return map;
	}

    @LogAnnotation(moduleName = "收支项目字典", option = "修改")
	@Override
	public StateInfo edit(String pid, ItemType itemType, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(pid) && itemType != null) {
			Map<String,Object> map = this.getBeanInfoMap(itemType,bridge);
			map.put("whereId",pid);
			try {
				this.itemTypeDao.update(map);
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

    @LogAnnotation(moduleName = "收支项目字典", option = "新增")
	@Override
	public StateInfo add(String parentId,ItemType itemType, Bridge bridge) {
		//tree init
        itemType.setIsleaf(1);
		if(!CommonUtil.isEmpty(parentId)) {
                itemType.setParentid(parentId);
		}else {
                itemType.setParentid(Global.NULLSTRING);
		}
		//自动生成主键按1 101 10101规则
//		Map<String,Object> mapOfID = this.itemTypeDao.getAutoGeneralID(parentId,bridge);
//		itemType.setPid(CommonUtil.dealPKRule(mapOfID,parentId));
		Map<String,Object> map = this.getBeanInfoMap(itemType,bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			if(!CommonUtil.isEmpty(parentId)) {
				Map<String,Object> map2 = new HashMap<>();
				map2.put("year", bridge.getYear());
				map2.put("id",parentId);
				map2.put("value","0");
				//父节点leaf 设置为0
				this.itemTypeDao.changeLeaf(map2);
			}
			this.itemTypeDao.insert(map);
		}catch(Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	/**
	 *初始化内置数据
	 */
     @LogAnnotation(moduleName = "收支项目字典", option = "初始化数据")
	 @Override
	 public StateInfo autoInitFromAccess(Bridge bridge){
	 	StateInfo stateInfo = new StateInfo();
	 	Connection conn = null;
	 	Statement stmt = null;
	 	ResultSet rs = null;
	 	try{
			this.itemTypeDao.deleteAll(bridge.getYear());
			conn = AccessOptUtil.connectAccessFile();
            if(conn != null) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("Select * From PD_ITEMTYPE");
                List<String> sqlList = new ArrayList<>();
                StringBuilder sqlBuffer = new StringBuilder();
                while(rs.next()) {
                    sqlBuffer.setLength(0);
                    sqlBuffer.append("Insert Into PD_ITEMTYPE");
                    sqlBuffer.append("(pid,companyid,code,text,itemtype,parentid,isleaf,isstop) values (");
                    sqlBuffer.append("'").append(rs.getString("pid")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(bridge.getCompanyid()).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("code")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("text")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("itemtype")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("parentid")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("isleaf")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("isstop"));
                
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
}