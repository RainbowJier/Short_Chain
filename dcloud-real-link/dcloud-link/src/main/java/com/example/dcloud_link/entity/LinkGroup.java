package com.example.dcloud_link.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.io.Serializable;

/**
 * (LinkGroup)实体类
 *
 * @author makejava
 * @since 2024-10-29 14:04:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("link_group")
public class LinkGroup implements Serializable {
    private static final long serialVersionUID = -74132650745233741L;

    private Long id;
    /**
     * 组名
     */
    private String title;
    /**
     * 账号唯一编号
     */
    private Long accountNo;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

}

