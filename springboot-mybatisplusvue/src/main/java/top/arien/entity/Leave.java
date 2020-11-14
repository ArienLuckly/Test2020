package top.arien.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


/**
 * @author Arien-天柱
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("m_leave")
public class Leave {

    @TableId(type = IdType.AUTO)
    private Integer lid;

    @NotBlank(message = "用户id不能为空")
    private Integer uid;

    private Integer rid;

    @NotBlank(message = "留言信息不能为空")
    private String lmessage;

    private String lcreated;

    private User user;
}
