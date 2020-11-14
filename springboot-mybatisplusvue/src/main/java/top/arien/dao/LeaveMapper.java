package top.arien.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.arien.entity.Leave;

import java.util.List;

/**
 * @author Arien-天柱
 **/
public interface LeaveMapper {

    //存储用户的留言
    void save(Integer uid, String lmessage, String lcreated);

    //查询所有留言信息
    List<Leave> getLeave();

    IPage<Leave> findAllAndPage(IPage<Leave> page);

    //保存rid信息
    void saveRid(Integer rid);
}
