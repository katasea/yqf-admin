package com.common.excel;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
@Component
public class DictUtil {
     public static Map<String,Map<String,String>> dictMap = new  HashMap<String,Map<String,String>>();//字典map <type,<value,name>>
     public static Map<String,Map<String,String>> dictValueMap = new  HashMap<String,Map<String,String>>();//字典map <type,<name,value>>
     
	 public static final Map<String,String>  xingBieMap = new HashMap<String,String>(); //会员性别 1:男,2:女
	 public static final Map<String,String>  xingBieValueMap = new HashMap<String,String>(); //会员性别
	 
	 public static final Map<String,String>  huiYuanDengJiMap = new HashMap<String,String>();//会员等级  1:普通会员,2:VIP会员,3:酒店员工
	 public static final Map<String,String>  huiYuanDengJiValueMap = new HashMap<String,String>();//会员等级
	 
     public static final Map<String,String>  huiYuanZhuangTaiMap = new HashMap<String,String>();//会员状态  1:正常,2:禁用
     public static final Map<String,String>  huiYuanZhuangTaiValueMap = new HashMap<String,String>();//会员状态
     
     public static final Map<String,String>  zhiFuLeiXingMap = new HashMap<String,String>();//支付类型 1.订房 2充值 3退款 4升级vip
     public static final Map<String,String>  zhiFuLeiXingValueMap = new HashMap<String,String>();//支付类型
     
     public static final Map<String,String>  dingDanZhuangTaiMap = new HashMap<String,String>();//订单状态(1:待确认,2:已确认,3: 已取消 4 已评价)
     public static final Map<String,String>  dingDanZhuangTaiValueMap = new HashMap<String,String>();
     
     public static final Map<String,String> duanXinFaSongMap =  new HashMap<String,String>();//短信发送(0:未发送,1:已发送)
     public static final Map<String,String> duanXinFaSongValueMap =  new HashMap<String,String>();
     
     public static final Map<String,String> ruZhuZhuangTaiMap =  new HashMap<String,String>();//入住状态(0:未入住,1:已入住)
     public static final Map<String,String> ruZhuZhuangTaiValueMap =  new HashMap<String,String>();
     
     public static final Map<String,String> zhiFuZhuangTaiMap =  new HashMap<String,String>();//支付状态 0未支付 1已支付
     public static final Map<String,String> zhiFuZhuangTaiValueMap =  new HashMap<String,String>();
     
     public static final Map<String,String> vipBianDongMap =  new HashMap<String,String>();//vip变动类型 1在线 2手动 3积分
     public static final Map<String,String> vipBianDongValueMap =  new HashMap<String,String>();
     
     public static final Map<String,String> shiYongZhuangTaiMap =  new HashMap<String,String>();//使用状态 0 未使用 1已使用 2已过期
     public static final Map<String,String> shiYongZhuangTaiValueMap =  new HashMap<String,String>();

	static{
		
		DictUtil.xingBieMap.put("1","男");
		DictUtil.xingBieMap.put("2","女");
		DictUtil.xingBieValueMap.put("男","1");
		DictUtil.xingBieValueMap.put("女","2");
		
		DictUtil.huiYuanDengJiMap.put("1","普通会员");
		DictUtil.huiYuanDengJiMap.put("2","vip会员");
		DictUtil.huiYuanDengJiMap.put("3","酒店员工");
		DictUtil.huiYuanDengJiValueMap.put("普通会员","1");
		DictUtil.huiYuanDengJiValueMap.put("vip会员","2");
		DictUtil.huiYuanDengJiValueMap.put("酒店员工","3");
		
		DictUtil.huiYuanZhuangTaiMap.put("1","正常");
		DictUtil.huiYuanZhuangTaiMap.put("2","禁用");
		DictUtil.huiYuanZhuangTaiValueMap.put("正常","1");
		DictUtil.huiYuanZhuangTaiValueMap.put("禁用","2");
		
		DictUtil.zhiFuLeiXingMap.put("1","订房");
		DictUtil.zhiFuLeiXingMap.put("2","充值");
		DictUtil.zhiFuLeiXingMap.put("3","退款");
		DictUtil.zhiFuLeiXingMap.put("4","升级vip");
		DictUtil.zhiFuLeiXingValueMap.put("订房","1");
		DictUtil.zhiFuLeiXingValueMap.put("充值","2");
		DictUtil.zhiFuLeiXingValueMap.put("退款","3");
		DictUtil.zhiFuLeiXingValueMap.put("升级vip","4");
		
		
		DictUtil.dingDanZhuangTaiMap.put("1","待确认");
		DictUtil.dingDanZhuangTaiMap.put("2","已确认");
		DictUtil.dingDanZhuangTaiMap.put("3","已取消");
		DictUtil.dingDanZhuangTaiMap.put("4","已评价");
		
		DictUtil.duanXinFaSongMap.put("0","未发送");
		DictUtil.duanXinFaSongMap.put("1","已发送");
		
		DictUtil.ruZhuZhuangTaiMap.put("0","未入住");
		DictUtil.ruZhuZhuangTaiMap.put("1","已入住");
		
		DictUtil.zhiFuZhuangTaiMap.put("0","未支付");
		DictUtil.zhiFuZhuangTaiMap.put("1","已支付");
		
		DictUtil.vipBianDongMap.put("1","在线");
		DictUtil.vipBianDongMap.put("2","手动");
		DictUtil.vipBianDongMap.put("3","积分");
		
		
		
		DictUtil.shiYongZhuangTaiMap.put("0","未使用");
		DictUtil.shiYongZhuangTaiMap.put("1","已使用");
		DictUtil.shiYongZhuangTaiMap.put("2","已过期");
		
		
		//导出map
		DictUtil.dictMap.put("xingBie", DictUtil.xingBieMap);
		DictUtil.dictMap.put("huiYuanDengJi", DictUtil.huiYuanDengJiMap);
		DictUtil.dictMap.put("huiYuanZhuangTai", DictUtil.huiYuanZhuangTaiMap);
		DictUtil.dictMap.put("zhiFuLeiXing", DictUtil.zhiFuLeiXingMap);
	    DictUtil.dictMap.put("dingDanZhuangTai",DictUtil.dingDanZhuangTaiMap);
	    DictUtil.dictMap.put("duanXinFaSong", DictUtil.duanXinFaSongMap);
	    DictUtil.dictMap.put("ruZhuZhuangTai", DictUtil.ruZhuZhuangTaiMap);
	    DictUtil.dictMap.put("zhiFuZhuangTai", DictUtil.zhiFuZhuangTaiMap);
	    DictUtil.dictMap.put("vipBianDong", DictUtil.vipBianDongMap);
	    DictUtil.dictMap.put("shiYongZhuangTai", DictUtil.shiYongZhuangTaiMap);
		
	    
	    //导入map(会员)
		DictUtil.dictValueMap.put("xingBie", DictUtil.xingBieValueMap);
		DictUtil.dictValueMap.put("huiYuanDengJi", DictUtil.huiYuanDengJiValueMap);
		DictUtil.dictValueMap.put("huiYuanZhuangTai", DictUtil.huiYuanZhuangTaiValueMap);
		DictUtil.dictValueMap.put("zhiFuLeiXing",DictUtil.zhiFuLeiXingValueMap);
		DictUtil.dictValueMap.put("vipBianDong",DictUtil.vipBianDongValueMap);
		
		
		
		
		
	}
}
