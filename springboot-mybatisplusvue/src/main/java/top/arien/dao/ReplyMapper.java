package top.arien.dao;

import top.arien.entity.Reply;

import java.util.List;

/**
 * @author Arien-天柱
 **/
public interface ReplyMapper {

    //存储回复信息
    void saveReply(Integer rid, String rmessage, String rcreated, Integer ruid);

    //获取所有回复信息
    List<Reply> getReply();
}
