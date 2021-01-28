package com.main.dao.platform;

import java.util.List;
import java.util.Map;

import com.main.pojo.platform.NoticeInfo;
import org.apache.ibatis.annotations.Param;
import com.main.pojo.platform.Bridge;

/**
 * 对象持久层接口
 *
 * @author Tim[ATC.Pro Generate]
 */
public interface NoticeInfoDao {

	/**
	 * 获取树形对应节点数据
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:''}
	 * @return 对象集
	 */
	List<NoticeInfo> get(Map<String, Object> map);

	/**
	 * 获取对应数量
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:''}
	 * @return map {count:11}
	 */
	Map<String, Object> getCount(Map<String, Object> map);

	/**
	 * 获取列表数据
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:'',start:1,end:10}
	 * @return 对象集
	 */
	List<NoticeInfo> getPage(Map<String, Object> map);

	/**
	 * 获取所有数据包括停用
	 *
	 * @param companyid 单位id
	 * @return 对象集
	 */
	List<NoticeInfo> getAll(@Param("companyid") String companyid);

	/**
	 * 插入对象
	 *
	 * @param map 对象转map
	 * @return 无
	 */
	int insert(Map<String, Object> map);

	/**
	 * 修改对象
	 *
	 * @param map 对象转map
	 * @return 无
	 */
	int update(Map<String, Object> map);

	/**
	 * 通过主键删除对象
	 *
	 * @param map {year:2018,id:''}
	 * @return 无
	 */
	int deleteByPrimaryKey(Map<String, Object> map);


	/**
	 * 通过主键获取对象
	 *
	 * @param map {id:''}
	 * @return 对象
	 */
	NoticeInfo selectBypkid(Map<String, Object> map);

	/**
	 * 验证数据是否存在，返回具体数据
	 *
	 * @param key    字段
	 * @param value  值
	 * @param bridge 桥梁对象
	 * @return 查询结果
	 */
	List<Map<String, Object>> validator(@Param("key") String key, @Param("value") String value, @Param("bridge") Bridge bridge);

	/**
	 * 从数据看获取最大id用于自动计算生成id
	 *
	 * @param parentId 父节点
	 * @param bridge   桥梁对象
	 * @return map {id:''}
	 */
	Map<String, Object> getAutoGeneralID(@Param("parentId") String parentId, @Param("bridge") Bridge bridge);

	/**
	 * 删除所有记录
	 *
	 * @param year 年
	 */
	void deleteAll(@Param("year") String year);

	/**
	 * 维护用户与通知消息的关联关系
	 * 删除后插入
	 *
	 * @param noticeInfo 消息通知对象
	 * @param bridge     桥梁对象
	 * @param state      需要设置的状态 0 未读 1 已读
	 */
	void saveUserNoticeRela(@Param("noticeInfo") NoticeInfo noticeInfo, @Param("bridge") Bridge bridge, @Param("state") int state);

	/**
	 * 通过用户和状态获取已读或者未读的消息列表
	 *
	 * @param userid 用户id
	 * @param bridge 桥梁对象
	 * @param state  状态 0 未读 1 已读
	 * @return 消息通知列表
	 */
	List<NoticeInfo> getNoticeWithState(@Param("userid") String userid, @Param("bridge") Bridge bridge, @Param("state") int state);

	/**
	 * 删除用户和未读消息的关联关系
	 *
	 * @param noticepkid
	 */
	void deleteUserNoticeRela(@Param("noticepkid") String noticepkid);

	/**
	 * 修改某人的某条信息状态
	 * @param userid 用户账号
	 * @param pkid 消息主键
	 * @param state 0未读1已读
	 */
	void updateUserNoticeState(@Param("userid") String userid,@Param("noticepkid") String pkid,@Param("state") int state);
}
