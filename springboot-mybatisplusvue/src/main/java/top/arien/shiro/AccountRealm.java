package top.arien.shiro;

import cn.hutool.core.bean.BeanUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.arien.dao.UserDAO;
import top.arien.dao.UserMapper;
import top.arien.entity.User;
import top.arien.util.JwtUtils;
import top.arien.util.ShiroUtil;

/**
 * @author Arien-天柱
 **/
@Component
public class AccountRealm  extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserMapper userMapper;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //到数据库查询当前登陆用户的授权字符串
        //获取当前登陆用户
        System.out.println("用户授权");
        Subject subject = SecurityUtils.getSubject();
        //System.out.println(subject.getPrincipal());
        User user = (User) subject.getPrincipal();
        int realm = userMapper.selectAidById(user.getId());
        System.out.println(user);
        System.out.println(realm);
        if(realm == 1){
            info.addStringPermission("super");
        }else if(realm == 2){
            info.addStringPermission("user");
        }else{
            info.addStringPermission("visitor");
        }
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        User user = userDAO.selectById(Long.valueOf(userId));
        if(user==null){
            throw new UnknownAccountException("账户不存在");
        }
        if(user.getStatus()==-1){
            throw new LockedAccountException("账户已被锁定");
        }

        /*AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);*/
        System.out.println("-------------->>>");
        return new SimpleAuthenticationInfo(user, jwtToken.getCredentials(), getName());
    }
}
