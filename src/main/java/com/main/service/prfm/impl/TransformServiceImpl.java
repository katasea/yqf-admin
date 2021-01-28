package com.main.service.prfm.impl;

import com.main.dao.prfm.TransformDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.prfm.Transform;
import com.main.service.prfm.TransformService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TransformServiceImpl implements TransformService {

    @Resource
    TransformDao transformDao;

    @Override
    public List<Transform>  getDataByType(String typeid, Bridge bridge) {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("typeID",typeid);
        paramMap.put("companyID",bridge.getCompanyid());
        paramMap.put("theyear",bridge.getYear());
        paramMap.put("themonth",bridge.getMonth());

        return this.transformDao.getDataByType(paramMap);
    }

    @Override
    public boolean save(Transform transform) {

        if(transform.getId()!=null && !"null".equals(transform.getId())){
            return this.transformDao.update(transform);
        }else{
            return this.transformDao.save(transform);
        }
    }

    @Override
    public boolean del(Transform transform) {
         return this.transformDao.del(transform);
    }

    @Override
    public Map<String, Object> transform(Bridge bridge,String month1,String month2) {

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("success",true);

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("companyid",bridge.getCompanyid());
        paramMap.put("year",bridge.getYear());
        paramMap.put("month",bridge.getMonth());

        paramMap.put("tableName","");
        //1.删除当月旧的原始数据
        transformDao.deleteOriginalData(paramMap);
        //2.原始数据导入
        transformDao.insertOriginalData(paramMap);

        //执行门诊的转换
        paramMap.put("tableName","PD_ZY");
        //转换开单科室
        paramMap.put("target","sbmbh");
        paramMap.put("typeId","1");
        transformDao.transformItem(paramMap);

        //转换执行科室
        paramMap.put("target","zbmbh");
        paramMap.put("typeId","1");
        transformDao.transformItem(paramMap);

        //转换开单人
        paramMap.put("target","sgrbh");
        paramMap.put("typeId","3");
        transformDao.transformItem(paramMap);

        //转换执行人
        paramMap.put("target","zgrbh");
        paramMap.put("typeId","3");
        transformDao.transformItem(paramMap);

        //转换核算项目
        paramMap.put("target","itembh");
        paramMap.put("typeId","4");
        transformDao.transformItem(paramMap);


        //执行住院的转换
        paramMap.put("tableName","PD_ZY");
        //转换开单科室
        paramMap.put("target","sbmbh");
        paramMap.put("typeId","2");
        transformDao.transformItem(paramMap);

        //转换执行科室
        paramMap.put("target","zbmbh");
        paramMap.put("typeId","2");
        transformDao.transformItem(paramMap);

        //转换开单人
        paramMap.put("target","sgrbh");
        paramMap.put("typeId","3");
        transformDao.transformItem(paramMap);

        //转换执行人
        paramMap.put("target","zgrbh");
        paramMap.put("typeId","3");
        transformDao.transformItem(paramMap);

        //转换核算项目
        paramMap.put("target","itembh");
        paramMap.put("typeId","4");
        transformDao.transformItem(paramMap);

        //获取转换后调整
        List<Transform> transformAfter = this.getDataByType("6",bridge);
        if( transformAfter!= null && transformAfter.size()>0){

            for(Transform transform:transformAfter){
                String[] conditionIds = transform.getCondition_id().split(",");
                String[] conditionValues = transform.getCondition_value().split(",");
                String[] adjustmentIds = transform.getAdjustment_id().split(",");
                String[] adjustmentValues = transform.getAdjustment_value().split(",");

                paramMap.put("conditionIds",conditionIds);
                paramMap.put("conditionValues",conditionValues);
                paramMap.put("adjustmentIds", adjustmentIds);
                paramMap.put("adjustmentValues",adjustmentValues);

                //转换后调整
                transformDao.transformAfter(paramMap);
            }

        }


        return resultMap;
    }
}
