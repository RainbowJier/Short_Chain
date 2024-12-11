package entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Traffic)实体类
 *
 * @author RainbowJier
 * @since 2024-12-11 17:02:29
 */
public class Traffic implements Serializable {
    private static final long serialVersionUID = 575466528452014002L;

    private Long id;
/**
     * Daily limit for short links
     */
    private Integer dayLimit;
/**
     * Number of short links used for the day
     */
    private Integer dayUsed;
/**
     * Total limit, used for live codes
     */
    private Integer totalLimit;
/**
     * Account number
     */
    private Long accountNo;
/**
     * Order number
     */
    private String outTradeNo;
/**
     * Product level: FIRST(Bronze), SECOND(Gold), THIRD(Diamond)
     */
    private String level;
/**
     * Expiration date
     */
    private Date expiredDate;
/**
     * Plugin type
     */
    private String pluginType;
/**
     * Product primary key
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

