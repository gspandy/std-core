package com.cdkj.core.ao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdkj.core.ao.IInteractAO;
import com.cdkj.core.bo.IInteractBO;
import com.cdkj.core.bo.base.Paginable;
import com.cdkj.core.common.DateUtil;
import com.cdkj.core.core.OrderNoGenerater;
import com.cdkj.core.domain.Interact;
import com.cdkj.core.dto.req.XN801030Req;
import com.cdkj.core.dto.req.XN801031Req;
import com.cdkj.core.enums.EGeneratePrefix;
import com.cdkj.core.enums.EInteractCategory;
import com.cdkj.core.enums.EInteractType;
import com.cdkj.core.exception.BizException;

@Service
public class InteractAOImpl implements IInteractAO {

    @Autowired
    private IInteractBO interactBO;

    @Override
    public String addInteract(XN801030Req req) {
        EInteractCategory.getMap().containsKey(req.getCategory());
        EInteractType.getMap().containsKey(req.getType());
        String code = null;
        if (!EInteractType.SCAN.getCode().equals(req.getType())) {
            interactBO.doCheckExist(req.getInteracter(), req.getCategory(),
                req.getType(), req.getEntityCode());
            List<Interact> interactList = interactBO.queryInteractList(
                req.getCategory(), req.getType(), req.getEntityCode(),
                req.getInteracter(), req.getCompanyCode(), req.getSystemCode());
            if (CollectionUtils.isNotEmpty(interactList)) {
                throw new BizException("xn0000", "您已收藏该"
                        + EInteractType.getMap().get(req.getType()).getValue());
            }
        } else {
            Long number = interactBO.totalInteract(req.getCategory(),
                req.getType(), req.getEntityCode(), DateUtil.getTodayStart(),
                DateUtil.getTodayEnd(), req.getCompanyCode(),
                req.getSystemCode());
            if (number > 0) {
                return code;
            }
        }

        Interact data = new Interact();
        code = OrderNoGenerater.generate(EGeneratePrefix.Interact.getCode());
        data.setCode(code);
        data.setCategory(req.getCategory());
        data.setType(req.getType());
        data.setEntityCode(req.getEntityCode());
        data.setInteracter(req.getInteracter());
        data.setInteractDatetime(new Date());
        data.setCompanyCode(req.getCompanyCode());
        data.setSystemCode(req.getSystemCode());
        interactBO.saveInteract(data);
        return code;
    }

    @Override
    public void dropInteract(XN801031Req req) {
        EInteractCategory.getMap().containsKey(req.getCategory());
        EInteractType.getMap().containsKey(req.getType());
        if (req.getType().equals(EInteractType.SCAN.getCode())) {
            throw new BizException("xn0000", "浏览类型不能取消");
        }
        List<Interact> interactList = interactBO.queryInteractList(
            req.getCategory(), req.getType(), req.getEntityCode(),
            req.getInteracter(), req.getCompanyCode(), req.getSystemCode());
        if (CollectionUtils.isNotEmpty(interactList)) {
            for (Interact interact : interactList) {
                interactBO.removeInteract(interact);
            }
        } else {
            throw new BizException("xn0000", "您未成功"
                    + EInteractType.getMap().get(req.getType()).getValue()
                    + "过,不能取消");
        }
    }

    @Override
    public Paginable<Interact> queryInteractPage(int start, int limit,
            Interact condition) {
        Paginable<Interact> page = interactBO.getPaginable(start, limit,
            condition);
        List<Interact> list = page.getList();
        for (Interact interact : list) {
            interact.setInteractDatetimeTimes(interact.getInteractDatetime()
                .getTime());
            interact.setInteractDatetime(null);
        }
        return page;
    }

    @Override
    public List<Interact> queryInteractList(Interact condition) {
        return interactBO.queryInteractList(condition);
    }

    @Override
    public Interact getInteract(String code, String companyCode,
            String systemCode) {
        return interactBO.getInteract(code, companyCode, systemCode);
    }

    @Override
    public boolean isInteract(String userId, String category, String type,
            String entityCode, String companyCode, String systemCode) {
        return interactBO.isInteract(userId, category, type, entityCode,
            companyCode, systemCode);
    }

    @Override
    public Long totalInteract(String category, String type, String entityCode,
            String companyCode, String systemCode) {
        return interactBO.totalInteract(category, type, entityCode, null, null,
            companyCode, systemCode);
    }
}
