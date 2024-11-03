package com.example.dcloud_link.manager;

import com.example.dcloud_link.entity.ShortLink;

/**
 * (ShortLink)表服务接口
 *
 * @author makejava
 * @since 2024-10-29 14:11:16
 */
public interface ShortLinkManager {

    /**
     * 新增短链
     */
    int addShortLink(ShortLink shortLink);

    /**
     * 根据短链码查询短链
     */
    ShortLink findbyShortLink(int shortLinkCode);

    /**
     * 根据短链码和 accountNo 删除
     */
    int del(String shortLinkCode, Long accountNo);
}
