package top.arien.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.arien.entity.Blog;
import top.arien.entity.Leave;
import top.arien.entity.User;

import java.util.List;

/**
 * @author Arien-天柱
 **/
public interface UserMapper {

    //查找是否存在邮箱
    public Integer selectByEmail(String emial);

    //查找是否存在该姓名
    public Integer selectByName(String username);

    //保存用户
    public void saveUser(User user);

    //查找文章标题
    public List<Blog> searchTitle(String query);

    //查找用户权限
    public Integer selectAidById(Integer id);

}
