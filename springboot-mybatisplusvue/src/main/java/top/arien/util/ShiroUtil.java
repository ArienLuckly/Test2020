package top.arien.util;

import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import top.arien.shiro.AccountProfile;

/**
 * @author Arien-天柱
 **/
public class ShiroUtil {

    public static AccountProfile getProfile(){
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
