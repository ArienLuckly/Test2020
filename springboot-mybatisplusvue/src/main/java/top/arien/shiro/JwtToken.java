package top.arien.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Arien-天柱
 **/
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String jwt){
        this.token = jwt;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
