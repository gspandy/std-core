package com.cdkj.core.api.impl;

import com.cdkj.core.ao.IPublicityAO;
import com.cdkj.core.api.AProcessor;
import com.cdkj.core.common.JsonUtil;
import com.cdkj.core.core.StringValidater;
import com.cdkj.core.dto.req.XN801064Req;
import com.cdkj.core.dto.res.BooleanRes;
import com.cdkj.core.exception.BizException;
import com.cdkj.core.exception.ParaException;
import com.cdkj.core.spring.SpringContextHolder;

/**
 * 下架宣传类活动
 * @author: asus 
 * @since: 2017年10月10日 下午1:17:24 
 * @history:
 */
public class XN801064 extends AProcessor {
    private IPublicityAO publicityAO = SpringContextHolder
        .getBean(IPublicityAO.class);

    private XN801064Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        publicityAO.putOff(req.getCode(), req.getUpdater(), req.getRemark());
        return new BooleanRes(true);
    }

    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN801064Req.class);
        StringValidater.validateBlank(req.getCode(), req.getUpdater());
    }
}
