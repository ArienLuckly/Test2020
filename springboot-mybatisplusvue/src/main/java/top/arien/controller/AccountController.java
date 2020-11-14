package top.arien.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.arien.common.dto.LoginDto;
import top.arien.common.lang.Result;
import top.arien.dao.UserDAO;
import top.arien.dao.UserMapper;
import top.arien.entity.User;
import top.arien.util.JwtUtils;
import top.arien.util.MD5Util;
import top.arien.util.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Arien-天柱
 **/
@RestController
public class AccountController {

    @Autowired
    UserDAO userDAO;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserMapper userMapper;

    //登陆
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){
        System.out.println("登陆测试！");
        //条件查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //检查是否存在该用户 用户姓名
        List<User> user = userDAO.selectList(queryWrapper.eq("username", loginDto.getUsername()));
        Assert.notNull(user, "用户不存在");

        User myuser = null;
        
        for(User us:user){
            myuser = us;
            //System.out.println("得到的myuser值为："+myuser);
            if(!us.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
                return Result.fail("密码不正确");
            }
        }
        if( myuser == null){
            return Result.fail("账号异常，请联系管理员！");
        }
        String jwt = jwtUtils.generateToken(myuser.getId());
        //System.out.println("输出一下加密的userid："+jwt);
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        return Result.success(MapUtil.builder()
                .put("id", myuser.getId())
                .put("username", myuser.getUsername())
                .put("avatar", myuser.getAvatar())
                .put("email", myuser.getEmail())
                .put("aid", myuser.getAid())
                .map()
        );
    }

    //发送验证码
    @RequestMapping(value="/getvarify")
    public Result getVarify(@RequestBody Map<String,Object> datas, HttpServletRequest request) {
        String email = (String)datas.get("email");
        String name = (String)datas.get("name");
        System.out.println("获取验证码:"+email+"获取到的姓名："+name);
        String regx = "(^\\w{3,}(\\.\\w+)*@[A-z0-9]+(\\.[A-z]{2,5}){1,2}$)";
        //检查邮箱是否已经注册
        Integer integer = userMapper.selectByEmail(email);
        //先判断邮箱是否存在
        if(email==""||email==null) {
            return Result.fail("邮箱不能为空！");
        }else if(integer == 1) {
            return Result.fail("该邮箱已经注册了！");
        }else if(!email.matches(regx)) {
            return Result.fail("请输入正确邮箱哦！");
        }else {
            int var = (int)((Math.random()*9+1)*10000);
            //放入session域后，其他请求也会取到sessionMessage 值
            request.getSession().setAttribute("var", var);
            System.out.println("验证码："+var);
            //String verificationCode = String.valueOf((int)((Math.random()*9+1)*1000));
            test.sendMail(email,var,name);
        }
        return Result.success("邮箱发送成功！");

    }

    //注册
    @PostMapping("/newUser")
    public Result newUser(@RequestBody @Validated User user,HttpServletRequest request){
        String ovar = null;
        String valid = user.getValid();
        System.out.println("注册测试！");
        System.out.println("用户输入的验证码为:"+valid);
        //条件查询
        Integer integer = userMapper.selectByEmail(user.getEmail());
        Integer integer02 = userMapper.selectByName(user.getUsername());
        int myemial = integer;
        int myname = integer02;
        ovar = request.getSession().getAttribute("var").toString();
        System.out.println("系统存储的验证码:"+ovar);

        //检验邮箱是否已经注册过了
        if(myemial == 1){
            return Result.fail("邮箱已经注册过了！");
        }else if(myname == 1){
            return Result.fail("该姓名已经存在了！");
        }else if(!ovar.contentEquals(valid)){
            return Result.fail("验证码输入错误！");
        }else{
            //随机头像
            String[] imgurl = {"http://www.arien.top/image/userheader/1.png", "http://www.arien.top/image/userheader/2.png", "http://www.arien.top/image/userheader/3.png"
                    , "http://www.arien.top/image/userheader/4.png", "http://www.arien.top/image/userheader/5.png", "http://www.arien.top/image/userheader/6.png"
            };
            Random rand = new Random();
            int num = rand.nextInt(3);
            user.setAvatar(imgurl[num]);
            //存储用户
            user.setPassword(MD5Util.getMD5(user.getPassword()));
            user.setAid(2);
            userMapper.saveUser(user);

            return Result.success(MapUtil.builder()
                    .put("id", user.getId())
                    .put("username", user.getUsername())
                    .put("avatar", user.getAvatar())
                    .put("email", user.getEmail())
                    .put("aid", user.getAid())
                    .map());
        }


    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout(){
        System.out.println("退出测试");
        SecurityUtils.getSubject().logout();
        return Result.success(null);
    }
}
