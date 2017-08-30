package com.cdkj.core.bo.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cdkj.core.bo.ISmsOutBO;
import com.cdkj.core.dto.req.XN001200Req;
import com.cdkj.core.http.BizConnecter;
import com.cdkj.core.http.JsonUtils;

/** 
 * @author: xieyj 
 * @since: 2016年5月25日 上午8:15:46 
 * @history:
 */
@Component
public class SmsOutBOImpl implements ISmsOutBO {
    static Logger logger = Logger.getLogger(SmsOutBOImpl.class);

    @Override
    public void sentContent(String ownerId, String content) {
        try {
            XN001200Req req = new XN001200Req();
            req.setTokenId(ownerId);
            req.setUserId(ownerId);
            req.setContent(content);
            BizConnecter.getBizData("001200", JsonUtils.object2Json(req),
                Object.class);
        } catch (Exception e) {
            logger.error("调用短信发送服务异常, 原因：" + e.getMessage());
        }
    }
}
