package com.std.activity.bo;

public interface ISmsOutBO {
    /**
     * 发送短信
     * @param userId
     * @param content 
     * @create: 2015年11月17日 下午3:37:54 myb858
     * @history:
     */
    void sentContent(String userId, String content);
}
