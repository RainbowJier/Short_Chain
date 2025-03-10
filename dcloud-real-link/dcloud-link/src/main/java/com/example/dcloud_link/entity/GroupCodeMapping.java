package com.example.dcloud_link.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (GroupCodeMapping0)实体类
 *
 * @author makejava
 * @since 2024-11-08 09:45:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@TableName("group_code_mapping")
public class GroupCodeMapping implements Serializable {

    private Long id;
    /**
     * 分组
     */
    private Long groupId;
    /**
     * 短链标题
     */
    private String title;
    /**
     * 原始url地址
     */
    private String originalUrl;
    /**
     * 短链域名
     */
    private String domain;
    /**
     * 短链压缩码
     */
    private String code;
    /**
     * 长链的md5码，方便查找
     */
    private String sign;
    /**
     * 过期时间，长久就是-1
     */
    private Date expired;
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
    /**
     * 0是默认，1是删除
     */
    private Long del;
    /**
     * 状态，lock是锁定不可用，active是可用
     */
    private String state;
    /**
     * 链接产品层级：FIRST 免费青铜、SECOND黄金、THIRD钻石
     */
    private String linkType;

}

