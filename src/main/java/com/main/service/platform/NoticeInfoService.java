package com.main.service.platform;

import com.main.pojo.platform.NoticeInfo;
import com.main.pojo.platform.StateInfo;

import java.util.List;

import com.main.pojo.platform.Bridge;

/**
 * 对象服务层接口
 *
 * @author Tim[ATC.Pro Generate]
 */
public interface NoticeInfoService {
	/**
	 * 通过主键获取部门信息
	 *
	 * @param id pkid主键
	 * @return 部门对象
	 */
	NoticeInfo selectBypkid(String id);

	/**
	 * 获取树
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @param view    是否查询当前用户的消息 非空为是 空为否
	 * @param type    0未读消息 1 历史消息
	 * @return 对象集
	 */
	List<NoticeInfo> get(String keyword, String node, Bridge bridge, String start, String limit, String view, String type);

	/**
	 * 获取当前条件下的对象数量
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @param view    是否查询当前用户的消息 非空为是 空为否
	 * @param type    0未读消息 1 历史消息
	 * @return 数量
	 */
	int getCount(String keyword, String node, Bridge bridge, String view, String type);

	/**
	 * 删除对象
	 *
	 * @param code     主键信息
	 * @param parentId 父节点信息
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo delete(String code, String parentId, Bridge bridge);

	/**
	 * 修改对象
	 *
	 * @param code       主键信息
	 * @param noticeInfo 修改对象
	 * @param bridge     桥梁对象
	 * @return 状态对象
	 */
	StateInfo edit(String code, NoticeInfo noticeInfo, Bridge bridge);

	/**
	 * 新增对象
	 *
	 * @param noticeInfo 新增对象
	 * @param bridge     桥梁对象
	 * @return 状态对象
	 */
	StateInfo add(NoticeInfo noticeInfo, Bridge bridge);

	/**
	 * 维护用户和未读消息的关联关系
	 * 删除后重新插入
	 *
	 * @param noticeInfo 消息通知对象
	 * @param bridge     桥梁对象
	 * @param state      0 未读 1 已读
	 * @return 状态对象
	 */
	StateInfo saveUserNoticeRela(NoticeInfo noticeInfo, Bridge bridge, int state);

	/**
	 * 获取消息列表，通过已读还是未读来获取指定用户的消息列表
	 *
	 * @param userid 用户id
	 * @param bridge 桥梁对象
	 * @param state  状态
	 * @return 消息通知列表
	 */
	List<NoticeInfo> getNoticeWithState(String userid, Bridge bridge, int state);

	/**
	 * 修改某人的某条信息状态
	 *
	 * @param pkid   消息主键
	 * @param userid 用户账号
	 * @return 状态对象
	 */
	StateInfo hadread(String pkid, String userid);
}