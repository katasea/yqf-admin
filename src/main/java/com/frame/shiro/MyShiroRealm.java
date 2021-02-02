package com.frame.shiro;

import com.common.Global;
import com.main.dao.platform.SystemDao;
import com.main.pojo.platform.ResourcesInfo;
import com.main.pojo.platform.RoleInfo;
import com.main.pojo.platform.UserInfo;
import com.main.service.platform.*;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 陈富强
 */
public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ResourcesInfoService resourcesService;

    @Resource
    private SystemDao systemDao;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserInfo user = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        List<ResourcesInfo> resourcesList = resourcesService.loadUserResources(user.getUserid());
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        StringBuilder urls = new StringBuilder();
        for (ResourcesInfo resource : resourcesList) {
            info.addStringPermission(resource.getResurl());
            urls.append(resource.getResurl()).append(";");
        }
        Logger.getLogger(this.getClass()).info("SHIRO RESOURCES：" + urls.toString());
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户的输入的账号.
        String userid = (String) token.getPrincipal();
        //获取user对象
        UserInfo user = userInfoService.selectByUserid(null,userid);
        if (user == null) throw new UnknownAccountException();
        if ("2".equals(user.getEnable())) {
            throw new LockedAccountException(); // 帐号锁定
        }else if("3".equals(user.getEnable())) {
            throw new UnknownAccountException();// 账号删除
        }
	    //获取角色
	    List<RoleInfo> roleInfos = systemDao.getRolesByUserid(user.getUserid());
	    user.setRoleInfos(roleInfos);
	    //判断超级管理员
	    user.setAdmin(false);
		for(RoleInfo roleInfo : roleInfos) {
			if("001".equals(roleInfo.getRoleid())) {
				user.setAdmin(true);
			}
		}

        Logger.getLogger(this.getClass()).info("SHIRO GET USER INFORMATION：" + user.getUserid() + ":" + user.getPassword());
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUserid(), //用户
                user.getPassword(), //密码
                ByteSource.Util.bytes(userid),
                getName()  //realm name
        );
        // 当验证都通过后，把用户信息放在session里
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("userSession", user);
        session.setAttribute("userSessionId", user.getUserid());

        return authenticationInfo;
    }
}

