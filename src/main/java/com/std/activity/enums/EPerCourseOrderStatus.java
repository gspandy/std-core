package com.std.activity.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author: shan 
 * @since: 2016年12月14日 下午1:09:48 
 * @history:
 */
public enum EPerCourseOrderStatus {
    NOTPAY("0", "待付款"), PAYSUCCESS("1", "付款成功"), RECEIVER_ORDER("2", "已接单"), HAVE_CLASS(
            "3", "已上课"), CLASS_OVER("4", "已下课"), USER_CANCEL("5", "用户取消"), PLAT_CANCEL(
            "6", "平台取消"), FINISH("7", "已完成");
    public static Map<String, EPerCourseOrderStatus> getEOrderStatusResultMap() {
        Map<String, EPerCourseOrderStatus> map = new HashMap<String, EPerCourseOrderStatus>();
        for (EPerCourseOrderStatus status : EPerCourseOrderStatus.values()) {
            map.put(status.getCode(), status);
        }
        return map;
    }

    EPerCourseOrderStatus(String code, String value) {
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
