package top.arien.shiro;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.arien.util.JwtUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * @author Arien-天柱
 * 进行登录处理
 **/
@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwt)){
            return null;
        }

        return new JwtToken(jwt);
    }


    //拦截，判断用户的jwt是否过期等
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        //System.out.println("拦截器获取到的token："+jwt);
        if(StringUtils.isEmpty(jwt)){
            return true;
        }else{
            //校验jwt
            Claims claim = jwtUtils.getClaimByToken(jwt);
            if(claim==null||jwtUtils.isTokenExpired(claim.getExpiration())){
                throw new ExpiredCredentialsException("token已经失效，请重新登陆！");
            }
            Subject subject = SecurityUtils.getSubject();
            //执行登录
            return   executeLogin(servletRequest, servletResponse);
        }


    }

//    @Override
//    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
//
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//
//        Throwable throwable = e.getCause() == null ? e : e.getCause();
//        Result result = Result.fail(throwable.getMessage());
//        String json = JSONUtil.toJsonStr(result);
//
//        try {
//            httpServletResponse.getWriter().print(json);
//        } catch (IOException ioException) {
//
//        }
//        return false;
//    }
//
//    @Override
//    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//
//        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
//        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
//        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
//        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
//        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
//        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
//        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
//            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
//            return false;
//        }
//
//        return super.preHandle(request, response);
//    }


}
