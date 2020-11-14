package top.arien.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.net.www.http.PosterOutputStream;
import top.arien.common.lang.Result;
import top.arien.dao.*;
import top.arien.entity.Blog;
import top.arien.entity.Leave;
import top.arien.entity.Reply;
import top.arien.entity.User;
import top.arien.util.ShiroUtil;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Arien-天柱
 **/
@RestController
public class BlogController {

    @Autowired
    BlogDAO blogDAO;

    @Autowired
    LeaveDao leaveDao;

    @Autowired
    UserMapper userMapper;

    @Autowired
    LeaveMapper leaveMapper;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ReplyMapper replyMapper;

    //分页
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage){
        //参数一：当前页默认值为1 参数二：每页显示数据记录数 默认值为10
        IPage<Blog> page = new Page<>(currentPage,5);
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();//获取查询条件对象
        //设置查询结果为倒序
        IPage<Blog> userIPage = blogDAO.selectPage(page, queryWrapper.orderByDesc("created"));
        //获取总记录数
        long total = userIPage.getTotal();
        System.out.println("总记录数："+total);
        userIPage.getRecords().forEach(user-> System.out.println("user"+user));
        return Result.success(userIPage);
    }

    //根据id查找博客
    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name="id")Long id){
        Blog blog = blogDAO.selectById(id);
        //判断博客是否为空
        Assert.notNull(blog, "该博客已被删除");

        return Result.success(blog);
    }

    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog){
        System.out.println("编辑！！！");
        Blog temp = null;
        //判断是否存在id，如果存在博客的id就是修改，如果不存在就是更新
        if(blog.getId() != null){
            System.out.println("获取到的博客编号"+blog.getId());
            temp = blogDAO.selectById(blog.getId());
            //只能编辑自己的文章
            //Assert.isTrue(temp.getUser_id() == ShiroUtil.getProfile().getId(), "没有权限编辑");
            BeanUtil.copyProperties(blog, temp, "id", "user_id", "created", "status");
            blogDAO.updateById(temp);
        }else{
            System.out.println("更新！！！");
            temp = new Blog();
            //从权限管理中获取id
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            Long id = Long.valueOf(user.getId());
            temp.setUser_id(id);
            //temp.setUser_id(new Long(1));
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
            BeanUtil.copyProperties(blog, temp, "id", "user_id", "created", "status");
            blogDAO.insert(temp);
        }



        return Result.success(null);
    }

    /*//查找文章标题
    @GetMapping("/search")
    public Result serchT(@RequestParam String query){
       *//* if(query==""||query==""){
            return Result.success("");
        }*//*
        System.out.println("执行查询操作！");
        List<Blog> strings = userMapper.searchTitle(query);
        return Result.success(strings);
    }*/

    //查找文章标题
    @GetMapping("/search")
    public Result serch2(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "") String query){
        //参数一：当前页默认值为1 参数二：每页显示数据记录数 默认值为10
        IPage<Blog> page = new Page<>(currentPage,5);
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();//获取查询条件对象
        //设置查询结果为倒序
        IPage<Blog> userIPage = blogDAO.selectPage(page, queryWrapper.like("title",query));
        //获取总记录数
        long total = userIPage.getTotal();
        System.out.println("总记录数："+total);
        userIPage.getRecords().forEach(user-> System.out.println("user"+user));
        return Result.success(userIPage);
    }


    //存储用户留言
    @PostMapping("/saveLeave")
    public Result saveLeave(@RequestBody Map<String,Object> datas){
        System.out.println("存储留言信息测试！");
        String uid = (String)datas.get("uid");
        Integer myuid = Integer.valueOf(uid);
        String lmessage = (String)datas.get("lmessage");
        String name = (String)datas.get("name");
        if(name==""||name.equals(null)){
            Date d = new Date();
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
            String date = sDateFormat.format(d);
            System.out.println("当前时间:"+date);
            leaveMapper.save(myuid,lmessage,date);
            return Result.success("留言成功！");
        }

        return Result.success("留言失败，有意见可以找博主哦！");
    }

    //获取所有用户留言
    @GetMapping("/getLeave")
    public Result getLeave(@RequestParam(defaultValue = "1") Integer currentPage){
        IPage<Leave> page = new Page<Leave>(currentPage,5);
        IPage<Leave> leave = leaveMapper.findAllAndPage(page);
        int total = (int)leave.getTotal();
        System.out.println("总记录数："+total);
        return Result.success(leave);
    }

    //获取所有用户
    @GetMapping("/getUser")
    public Result getUser(){
        List<User> users = userDAO.selectList(null);
        return Result.success(users);
    }
    
    //添加回复
    @PostMapping("/saveReply")
    public Result saveReplyT(@RequestBody Map<String,Object> datas){
        String uid = (String)datas.get("uid");
        Integer myuid = Integer.valueOf(uid);
        String rmessage = (String)datas.get("rmessage");
        Integer lid = (Integer) datas.get("lid");
        //Integer mylid = Integer.valueOf("lid");
        //String name = (String)datas.get("name");
        //获取当前时间
        if(rmessage.equals("")||rmessage.equals(null)){
            return Result.fail("留言内容不能为空！");
        }
        Date d = new Date();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
        String date = sDateFormat.format(d);
        //获取六位随机数
        //Integer randomsix = (int)((Math.random()*9+1)*100000);
        //首先存储到leave表中rid
        //leaveMapper.saveRid(randomsix);
        //然后存储到回复表中
        replyMapper.saveReply(lid,rmessage,date,myuid);
        return Result.success("回复成功！");
    }

    //获取所有回复
    @GetMapping("/getReply")
    public Result getAllReply(){
        List<Reply> reply = replyMapper.getReply();
        return Result.success(reply);
    }


    //测试权限
    @RequiresAuthentication
    @GetMapping("/test")
    public String test(){

        return "test";
    }

}
