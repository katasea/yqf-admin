package com.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonUtil {
	private static SerializeConfig innerConfig = new SerializeConfig();

	// 默认属性配置
	private static SerializerFeature[] innerFeatures = {
			SerializerFeature.DisableCircularReferenceDetect };

	static {
		innerConfig.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
		innerConfig.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
	}
	
	public static String toJson(Object obj) {
		return JSON.toJSONString(obj);
	}

    /**
     * 用于显示，不能用于存入redis
     * @param obj
     * @return
     */
    public static String getShowJson(Object obj) {
        SerializeWriter out = new SerializeWriter();
        try {
            JSONSerializer serializer = new JSONSerializer(out, innerConfig);
            for (com.alibaba.fastjson.serializer.SerializerFeature feature : innerFeatures) {
                serializer.config(feature, true);
            }
            serializer.write(obj);
            return out.toString();
        } catch(Exception e){
            return JsonUtil.toJson(obj);
        }finally {
            out.close();
        }
    }

	public static <T> T parseObject(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}

    public static <T> T objectToObjectByJson(Object object, Class<T> clazz) {
        return JSON.parseObject(JsonUtil.getShowJson(object), clazz);
    }
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseMap(String json) {
		return JSON.parseObject(json, Map.class);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseMap(Object obj) {
		return JSON.parseObject(getShowJson(obj), Map.class);
	}
	
	public static <T> List<T> parseList(String json, Class<T> clazz) {
		return JSONArray.parseArray(json, clazz);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}

	public static <T> T fromJson(String json, TypeReference<T> type) {
		return JSON.parseObject(json, type);
	}
	
	public static Object copyObject(Object source){
		if(source==null){
			return null;
		}
		String json=JsonUtil.getShowJson(source);
		return JsonUtil.fromJson(json, source.getClass());
	}


	/**
	 * @param json
	 * @return
	 */
	public static Map<String, Object> parseObjectMap(String json) {
		try{
			return JSON.parseObject(json, new ObjectMapRef());
		}catch(Exception e){
			return JsonUtil.parseMap(json);
		}
	}
    
    static class ObjectMapRef extends TypeReference<Map<String,Object>> {}

    static class StringMapRef extends TypeReference<Map<String,String>> {}
}
