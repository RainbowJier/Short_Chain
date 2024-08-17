package com.example.dcloudaccount.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author RainbowJier
 * @since 2024-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * account number
     */
    private Long accountNo;

    /**
     * avator
     */
    private String headImg;

    /**
     * phone number
     */
    private String phone;

    /**
     * password
     */
    private String pwd;

    /**
     * To process sensitive personal information.
     */
    private String secret;

    /**
     * email
     */
    private String mail;

    /**
     * username
     */
    private String username;

    /**
     * Certification Level: DEFAULT, REALNAME, ENTERPRISE, the numer of visits is different.
     */
    private String auth;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


}
