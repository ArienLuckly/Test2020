package top.arien.common.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Arien-天柱
 **/
@Data
public class LoginDto implements Serializable {

    @NotBlank(message = "昵称不能为空！")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Email
    private String email;
}
