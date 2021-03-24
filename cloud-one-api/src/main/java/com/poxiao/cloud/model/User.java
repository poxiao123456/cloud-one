package com.poxiao.cloud.model;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * user表
 * </p>
 *
 * @author zhangsan
 * @since 2020-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * username
     */
    @NotBlank(message = "用户名为空")
    private String username;

    /**
     * password
     */
    @NotBlank(message = "密码为空")
    private String password;

    /**
     * 头像地址
     */
    private String imageUri;

    /**
     * 创建时间
     */
    private LocalDateTime createDateTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateDateTime;

    /**
     * 删除状态（0：未删除，1：已删除）
     */
    private String deleteStatus;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
