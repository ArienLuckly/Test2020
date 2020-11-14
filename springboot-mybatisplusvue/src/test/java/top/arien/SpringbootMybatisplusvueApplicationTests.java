package top.arien;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.arien.dao.BlogDAO;
import top.arien.dao.LeaveMapper;
import top.arien.dao.ReplyMapper;
import top.arien.dao.UserMapper;
import top.arien.entity.Blog;
import top.arien.entity.Leave;
import top.arien.entity.User;

import java.util.List;

@SpringBootTest
class SpringbootMybatisplusvueApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    LeaveMapper leaveMapper;

    @Autowired
    ReplyMapper replyMapper;

    @Autowired
    BlogDAO blogDAO;

    @Test
    void contextLoads() {
        System.out.println(userMapper.selectByEmail("1158597462@qq.com"));
    }

    @Test
    void test01(){
        /*List<String> list = userMapper.searchTitle("我");*/
        //list.forEach(li-> System.out.println("查询到的值："+li));
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("age",23);//等值查询
        //年龄小于23的
        //queryWrapper.lt("age",24);
        //小于等于23岁的
        queryWrapper.eq("id",42);
        List<Blog> users = blogDAO.selectList(queryWrapper);
        users.forEach(user-> System.out.println(user));
    }

    @Test
    void test02(){
        //leaveMapper.save(1,"hello","2019-12-09");
        //replyMapper.saveReply(1,"1","1",1);

    }

    @Test
    void test03(){
        IPage<Leave> page=new Page<Leave>(1,2);
        IPage<Leave> leave = leaveMapper.findAllAndPage(page);
        /*leave.forEach(leave1 -> System.out.println(leave1));*/
    }




}
