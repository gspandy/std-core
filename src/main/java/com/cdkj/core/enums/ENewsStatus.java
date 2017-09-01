/**
 * @Title EProductStatus.java 
 * @Package com.xnjr.mall.enums 
 * @Description 
 * @author haiqingzheng  
 * @date 2016年5月17日 上午10:36:47 
 * @version V1.0   
 */
package com.cdkj.core.enums;

/** 
 * @author: haiqingzheng 
 * @since: 2016年5月17日 上午10:36:47 
 * @history:
 */
public enum ENewsStatus {
    TO_PUBLISH("0", "待发布"), PUBLISH_YES("1", "已上架"), PUBLISH_NO("2", "已下架");

    ENewsStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }

    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
