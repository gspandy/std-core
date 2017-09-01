package com.cdkj.core.api.impl;

import com.cdkj.core.ao.INewsAO;
import com.cdkj.core.api.AProcessor;
import com.cdkj.core.common.JsonUtil;
import com.cdkj.core.core.StringValidater;
import com.cdkj.core.dto.req.XN801003Req;
import com.cdkj.core.dto.res.BooleanRes;
import com.cdkj.core.exception.BizException;
import com.cdkj.core.exception.ParaException;
import com.cdkj.core.spring.SpringContextHolder;

/**
 * 上架资讯
 * @author: xieyj 
 * @since: 2017年8月31日 下午5:38:13 
 * @history:
 */
public class XN801003 extends AProcessor {
    private INewsAO newsAO = SpringContextHolder.getBean(INewsAO.class);

    private XN801003Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        Integer orderNo = StringValidater.toInteger(req.getOrderNo());
        newsAO.putOn(req.getCode(), req.getLocation(), orderNo,
            req.getUpdater(), req.getRemark());
        return new BooleanRes(true);
    }

    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN801003Req.class);
        StringValidater.validateBlank(req.getCode(), req.getLocation(),
            req.getUpdater());
        StringValidater.validateNumber(req.getOrderNo());
    }

}
