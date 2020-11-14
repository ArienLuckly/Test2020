package top.arien.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Arien-天柱
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("m_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "昵称不能为空！")
    private String username;

    private String avatar;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String password;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;

    private LocalDateTime last_login;

    private String valid;

    private Integer aid;
}
