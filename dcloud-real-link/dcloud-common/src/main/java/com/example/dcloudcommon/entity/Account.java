package com.example.dcloudcommon.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="Account对象", description="")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "account number")
    private Long accountNo;

    @ApiModelProperty(value = "avator")
    private String headImg;

    @ApiModelProperty(value = "phone number")
    private String phone;

    @ApiModelProperty(value = "password")
    private String pwd;

    @ApiModelProperty(value = "To process sensitive personal information.")
    private String secret;

    @ApiModelProperty(value = "email")
    private String mail;

    @ApiModelProperty(value = "username")
    private String username;

    @ApiModelProperty(value = "Certification Level: DEFAULT, REALNAME, ENTERPRISE, the numer of visits is different.")
    private String auth;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


}
