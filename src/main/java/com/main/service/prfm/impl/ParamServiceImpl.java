package com.main.service.prfm.impl;

import com.alibaba.fastjson.JSONObject;
import com.common.AccessOptUtil;
import com.common.CommonUtil;
import com.common.Global;
import com.main.dao.platform.CommonDao;
import com.main.dao.prfm.ParamDao;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.ParamBean;
import com.main.service.prfm.ParamService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class ParamServiceImpl implements ParamService {
    @Resource
    private ParamDao dao;
    @Resource
    private CommonDao commonDao;

    @Override
    public StateInfo insert(ParamBean paramBean) {
        StateInfo stateInfo = new StateInfo();
        try {
            paramBean.setParamsno(Global.createUUID());
            dao.insert(paramBean);
        } catch (Exception e) {
            String hint = "输入内容有误，请重新输入";
            stateInfo.setFlag(false);
            stateInfo.setMsg(this.getClass(), e.getMessage(), e);
            if (e instanceof org.springframework.dao.DuplicateKeyException) {
                hint = "添加的参数名称（主键）已存在，请修正后添加";
            }
            throw new RuntimeException(hint);
        }
        return stateInfo;
    }

    @Override
    public StateInfo update(ParamBean paramBean) {
        StateInfo stateInfo = new StateInfo();
        try {
            dao.updateByPrimaryKey(paramBean);
        } catch (Exception e) {
            String hint = "输入内容有误，请重新输入";
            stateInfo.setFlag(false);
            stateInfo.setMsg(this.getClass(), e.getMessage(), e);
            if (e instanceof org.springframework.dao.DuplicateKeyException) {
                hint = "添加的参数名称（主键）已存在，请修正后添加";
            }
            throw new RuntimeException(hint);
        }
        return stateInfo;
    }

    @Override
    public StateInfo delecte(String paramsno) {
        StateInfo stateInfo = new StateInfo();
        try {
            dao.deleteByPrimaryKey(paramsno);
        } catch (Exception e) {
            stateInfo.setFlag(false);
            stateInfo.setMsg(this.getClass(), e.getMessage(), e);
        }
        return stateInfo;
    }

    @Override
    public ParamBean get(String paramsno) {
        return dao.selectByPrimaryKey(paramsno);
    }

    @Override
    public int getCount(String keyWord, String paramstype) {
        return dao.getCount(keyWord, paramstype);
    }

    @Override
    public List<ParamBean> getPage(String keyWord, int start, int limit, String paramstype) {
        return dao.selectByPage(keyWord, start, start + limit - 1, paramstype);
    }

    @Override
    public StateInfo initData() {
        StateInfo stateInfo = new StateInfo();
        Connection conn = null;
        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            //获取Access链接，准备获取数据
            conn = AccessOptUtil.connectAccessFile();
            if (conn != null) {
                stmt = conn.createStatement();
                stmt2 = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM PD_PARAMS");

                List<String> sqlList = new ArrayList<>();
                StringBuilder sqlBuffer = new StringBuilder();
                while (rs.next()) {
                    sqlBuffer.setLength(0);
                    //删除同名数据
                    sqlBuffer.append("DELETE FROM PD_PARAMS WHERE PARAMSNO = '");
                    sqlBuffer.append(rs.getString("PARAMSNO")).append("'");
                    sqlList.add(sqlBuffer.toString());
                    sqlBuffer.setLength(0);
                    //插入access数据
                    sqlBuffer.append("INSERT INTO PD_PARAMS");
                    sqlBuffer.append("(PARAMSNO,PARAMSKEY,PARAMSVALUE,PARAMSTYPE,REVERSE1,REVERSE2) VALUES (");
                    sqlBuffer.append("'").append(rs.getString("PARAMSNO")).append("'");
                    sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("PARAMSKEY")).append("'");
                    sqlBuffer.append(",");
                    sqlBuffer.append("'").append(CommonUtil.isEmpty(rs.getString("PARAMSVALUE")) ? "" : rs.getString("PARAMSVALUE").replace("'", "''")).append("'");
                    sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("PARAMSTYPE")).append("'");
                    sqlBuffer.append(",");
                    sqlBuffer.append("'").append(CommonUtil.nullToStr(rs.getString("REVERSE1"))).append("'");
                    sqlBuffer.append(",");
                    sqlBuffer.append("'").append(CommonUtil.nullToStr(rs.getString("REVERSE2"))).append("'");
                    sqlBuffer.append(")");
                    sqlList.add(sqlBuffer.toString());
                }

	            //报表关联不显示的动态条件。
	            rs2 = stmt2.executeQuery("SELECT * FROM PD_REPORTPARAMRELA");
                while (rs2.next()) {
                    sqlBuffer.setLength(0);
                    //删除同名数据
                    sqlBuffer.append("DELETE FROM PD_REPORTPARAMRELA WHERE REPID = '");
                    sqlBuffer.append(rs2.getString("REPID")).append("'");
                    sqlList.add(sqlBuffer.toString());
                    sqlBuffer.setLength(0);
                    //插入access数据
                    sqlBuffer.append("INSERT INTO PD_REPORTPARAMRELA");
                    sqlBuffer.append("(UUID,REPID,PARAMSKEY) VALUES (");
                    sqlBuffer.append("sys_guid()");
                    sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs2.getString("REPID")).append("'");
                    sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs2.getString("PARAMSKEY")).append("'");
                    sqlBuffer.append(")");
                    sqlList.add(sqlBuffer.toString());
                }
                for (String sql : sqlList) {
                    commonDao.executeSQL(sql);
                }
            }
        } catch (Exception e) {
            stateInfo.setFlag(false);
            stateInfo.setMsg(this.getClass(), e.getMessage(), e);
            throw new RuntimeException();
        } finally {
            Global.close(conn, stmt, rs);
            Global.close(null, stmt2, rs2);
        }
        return stateInfo;
    }

    @Override
    public List<Map<String, Object>> getParamsValueData(ParamBean bean) {
        List<Map<String, Object>> resultMap = new ArrayList<>();
        try {

            if (bean != null) {
                String paramsvalue = bean.getParamsvalue();
                if (CommonUtil.isNotEmpty(paramsvalue) && paramsvalue.contains(Global.FORMULAJSONTOP)) {
                    paramsvalue = paramsvalue.replace(Global.FORMULAJSONTOP, "");
                    Object o = JSONObject.parse(paramsvalue);
                    resultMap = (List<Map<String, Object>>) o;
                } else if (CommonUtil.isNotEmpty(paramsvalue)) {
                    //手动添加别名
                    //先截取select 到 from 之间的语句
                    paramsvalue = paramsvalue.replace("from", "FROM");
                    String temp = paramsvalue.substring(7, paramsvalue.indexOf("FROM"));
                    String[] arr = temp.split(",");
                    if (arr.length == 2) {
                        paramsvalue = "SELECT " + arr[0] + " MKEY," + arr[1] + " MVALUE " + paramsvalue.substring(paramsvalue.indexOf("FROM"));
                        Logger.getLogger(this.getClass()).info("动态条件解析后SQL:" + paramsvalue);
                        resultMap = commonDao.getListInfos(paramsvalue);
                    }
                }
            } else {
                //没有找到参数记录
                Map<String, Object> map = new HashMap<>();
                map.put("未找到参数信息", "未找到参数信息");
                resultMap.add(map);
            }

        } catch (Exception e) {
            //可能主键丢失找到多条记录，或者其他异常。
            e.printStackTrace();
            Logger.getLogger(this.getClass()).error(e);
        }
        return resultMap;
    }

    @Override
    public Map<String, ParamBean> getMap() {
        //获取全部列表
        List<ParamBean> list = dao.selectByPage(null, 0, Global.TABLE_MAX_RECORD, "1");
        Map<String, ParamBean> resultMap = new HashMap<>();
        if (list != null) {
            for (ParamBean bean : list) {
                resultMap.put(bean.getParamskey(), bean);
                resultMap.put("[" + bean.getParamskey() + "]", bean);
            }
        }
        return resultMap;
    }

	@Override
	public Map<String, String> getRelaNoShowMap(String repid) {
    	List<Map<String,Object>> result = dao.getRela(repid);
    	Map<String,String> resultMap = new HashMap<>();
    	for(Map<String,Object> map : result) {
    		resultMap.put(String.valueOf(map.get("PARAMSKEY")), String.valueOf(map.get("PARAMSVALUE")));
	    }
		return resultMap;
	}
}
