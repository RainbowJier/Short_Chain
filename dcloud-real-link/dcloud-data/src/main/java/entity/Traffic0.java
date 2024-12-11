package entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Traffic0)实体类
 *
 * @author RainbowJier
 * @since 2024-12-11 17:03:43
 */
public class Traffic0 implements Serializable {
    private static final long serialVersionUID = -12519465481254364L;

    private Long id;
/**
     * 每天限制多少条
     */
    private Integer dayLimit;
/**
     * 当天用了多少条
     */
    private Integer dayUsed;
/**
     * 总次数，活码采用
     */
    private Integer totalLimit;
/**
     * 账号
     */
    private Long accountNo;
/**
     * 订单号
     */
    private String outTradeNo;
/**
     * Product level: FIRST(Bronze), SECOND(Gold), THIRD(Diamond)
     */
    private String level;
/**
     * 过期日期
     */
    private Date expiredDate;
/**
     * 插件类型
     */
    private String pluginType;
/**
     * 商品主键
     */
    private Long productId;

    private Date gmtCreate;

    private Date gmtModified;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }

    public Integer getDayUsed() {
        return dayUsed;
    }

    public void setDayUsed(Integer dayUsed) {
        this.dayUsed = dayUsed;
    }

    public Integer getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(Integer totalLimit) {
        this.totalLimit = totalLimit;
    }

    public Long getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Long accountNo) {
        this.accountNo = accountNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getPluginType() {
        return pluginType;
    }

    public void setPluginType(String pluginType) {
        this.pluginType = pluginType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

}

