package com.main.service.prfm.impl;

import com.common.MathUtil;
import com.main.dao.prfm.AllotDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.prfm.RbrvsCoefficient;
import com.main.pojo.prfm.Transform;
import com.main.service.prfm.AllotService;
import com.main.service.prfm.TransformService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class AllotServiceImpl implements AllotService {

    @Resource
    private AllotDao dao;

    @Resource
    private TransformService transformService;

    @Override
    @Transactional
    public Map<String, Object> calculateAllot(Bridge bridge) {

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("success",true);

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("companyId",bridge.getCompanyid());
        paramMap.put("year",bridge.getYear());
        paramMap.put("month",bridge.getMonth());
        //1.删除当月旧的工分结果
        dao.deleteAllotResult(paramMap);

        //2.获取门诊与住院的原始数据
        List<Map<String,Object>> oriList = dao.getOriMzAndZyData(paramMap);

        //3.分配
        allot(oriList,bridge);

        //4.结束

        return resultMap;
    }

    /**
     *    根据原始收入数据  以及对应的科室RBRVS系数进行分配计算。
     *    一条收入数据,根据申请,执行分为三条，申请，执行，护士。
     *    如果是执行部门的工分按照护士比例分给护士。
     *
     *    @param oriList 原始收入数据List
     *    @param bridge
     *
     * */
    public boolean allot(List<Map<String,Object>> oriList,Bridge bridge){

        //执行医生 护士对照
        Map<String,Object> docNurseRela = this.getDocNurseRelation(bridge);
        //rbrvs系数
        Map<String,Map<String,Object>> rbrvsXsMap = this.getRbrvsXsMap(bridge);

        for(Map<String,Object> map:oriList){

            //如果没有找到对应的项目的rbrvs系数，跳过.这边暂时不考虑特殊rbrvs系数~~~~~
            Map<String,Object> xsMap = rbrvsXsMap.get(map.get("itembh"));

            if(xsMap!=null) {
                String count = String.valueOf(map.get("count"));//获取数量
                String price = String.valueOf(xsMap.get("price"));//获取项目的单价
                String itemname = String.valueOf(xsMap.get("xmText"));//获取项目的名称
                String docRBRVS = String.valueOf(xsMap.get("doctor"));//医生系数
                String oriDocRBRVS = String.valueOf(xsMap.get("oriDoc"));//医生原始系数
                String adjDocRBRVS = String.valueOf(xsMap.get("adjDoc"));//医生调整系数

                map.put("price",price);
                map.put("itemname",itemname);
                //申请医生部门
                String sbmbh = String.valueOf(map.get("sbmbh"));//申请部门编号
                //原始数据 * RBRVS系数
                String sCount = MathUtil.cacComplex(count +"*"+ docRBRVS,4);
                map.put("sCount",sCount);//申请部门工分
                map.put("sRBRVS",docRBRVS);//最终系数
                map.put("sOriRBRVS",oriDocRBRVS);//原始系数
                map.put("sAdjRBRVS",adjDocRBRVS);//调整系数

                //执行医生部门
                String zbmbh = String.valueOf(map.get("zbmbh"));
                //原始数据 * RBRVS系数
                String zCount = MathUtil.cacComplex(count +"*"+ docRBRVS,4);
                map.put("zcount",zCount);//执行部门工分
                map.put("zRBRVS",docRBRVS);//最终系数
                map.put("zOriRBRVS",oriDocRBRVS);//原始系数
                map.put("zAdjRBRVS",adjDocRBRVS);//调整系数

                //如果护士部门不为空,则表示执行部门有对应护士部门，根据护士比例复制
                if(docNurseRela.get(zbmbh)!=null){
                    String nurseRBRVS = String.valueOf(xsMap.get("nurse"));
                    String nOriRBRVS = String.valueOf(xsMap.get("oriNurse"));
                    String nAdjRBRVS = String.valueOf(xsMap.get("adjNurse"));

                    //护士部门
                    String nurse = String.valueOf(docNurseRela.get(zbmbh));//执行科室对应的护士编号
                    String nCount = MathUtil.cacComplex(count +"*"+ nurseRBRVS,4);//护士工分
                    map.put("ncount",nCount);
                    map.put("nurse", nurse);//护士编号
                    map.put("nRBRVS", nurseRBRVS);//护士最终系数
                    map.put("nOriRBRVS",nOriRBRVS);//原始系数
                    map.put("nAdjRBRVS",nAdjRBRVS);//调整系数
                }

                dao.insertIntoAllotResult(map);
            }
        }

        return true;
    }

    /**
     *     获取执行医生->护士的对照
     *     返回  （医生，护士）map
     *
     * */
    public Map<String,Object> getDocNurseRelation(Bridge bridge){

        //获取执行医生 -> 护士对照的转换定义信息
        List<Transform> relaList = transformService.getDataByType("5",bridge);

        //组装成map
        Map<String,Object> resultMap = new HashMap<>();
        for(Transform transform:relaList){
           resultMap.put(transform.getCondition_id(),transform.getAdjustment_id());
        }

        return resultMap;
    }

    /**
     *     获取各项目的rbrvs系数
     *
     *     返回
     *     <项目编号,<doc/nurse/opt/...,系数>
     * */
    public Map<String,Map<String,Object>> getRbrvsXsMap(Bridge bridge){

        Map<String,Map<String,Object>> resultMap = new HashMap<>();

        List<RbrvsCoefficient> rbrvsList = dao.getRbrvsXsMap(bridge);

        //组装成结果map
        for (RbrvsCoefficient bean:rbrvsList){
            Map<String,Object> tempMap = new HashMap<>();
            if(bean.getXmbh()!=null){
                tempMap.put("oriDoc",bean.getOridoc());//医生原始系数
                tempMap.put("adjDoc",bean.getAdjdoc());//医生调整系数
                tempMap.put("doctor",bean.getDoc()); //医生最终系数

                tempMap.put("oriNurse",bean.getOrinurse());//护士原始系数
                tempMap.put("adjNurse",bean.getAdjnurse());//护士调整系数
                tempMap.put("nurse",bean.getNurse());//护士最终系数

                tempMap.put("oriPoision",bean.getOripoison());//麻醉原始系数
                tempMap.put("adjPoision",bean.getAdjpoison());//麻醉调整系数
                tempMap.put("poision",bean.getPoision());//麻醉最终系数

                tempMap.put("oriOperator",bean.getOrioperator());//医技原始系数
                tempMap.put("adjOperator",bean.getAdjoperator());//医技调整系数
                tempMap.put("operator",bean.getOperator());//医技最终技术
                tempMap.put("diagnosis",bean.getDiagnosis());//诊断最终系数
                tempMap.put("xmText",bean.getXmtext());//项目名
                tempMap.put("price",bean.getPrice());//单价
            }else{
                continue;
            }
            resultMap.put(bean.getXmbh(), tempMap);
        }
        return resultMap;
    }

}
