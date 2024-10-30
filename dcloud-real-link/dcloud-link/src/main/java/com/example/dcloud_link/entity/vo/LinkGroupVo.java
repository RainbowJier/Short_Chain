package com.example.dcloud_link.entity.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (LinkGroup)实体类
 *
 * @author makejava
 * @since 2024-10-29 14:04:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LinkGroupVo implements Serializable {

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
    private String gmtCreate;

    /**
     * 修改时间
     */
    private String gmtModified;

}

