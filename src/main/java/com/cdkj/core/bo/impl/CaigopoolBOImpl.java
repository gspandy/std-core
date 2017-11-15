package com.cdkj.core.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cdkj.core.bo.ICaigopoolBO;
import com.cdkj.core.bo.base.PaginableBOImpl;
import com.cdkj.core.dao.ICaigopoolDAO;
import com.cdkj.core.domain.Caigopool;
import com.cdkj.core.exception.BizException;

@Component
public class CaigopoolBOImpl extends PaginableBOImpl<Caigopool> implements
        ICaigopoolBO {

    @Autowired
    private ICaigopoolDAO stockPoolDAO;

    @Override
    public Caigopool getCaigopool() {
        Caigopool result = null;
        Caigopool condition = new Caigopool();
        List<Caigopool> dataList = stockPoolDAO.selectList(condition);
        if (CollectionUtils.isNotEmpty(dataList)) {
            result = dataList.get(0);
        } else {
            throw new BizException("xn0000", "池不存在");
        }
        return result;
    }

    @Override
    public Caigopool getCaigopool(String code) {
        Caigopool data = null;
        if (StringUtils.isNotBlank(code)) {
            Caigopool condition = new Caigopool();
            condition.setCode(code);
            data = stockPoolDAO.select(condition);
            if (data == null) {
                throw new BizException("xn0000", code + "对应的池不存在");
            }
        }
        return data;
    }

    @Override
    public int addAmount(Caigopool pool, Long czAmount, Long amount,
            String addUser, String remark) {
        pool.setCzAmount(czAmount + pool.getCzAmount());
        pool.setAmount(amount + pool.getAmount());
        pool.setAddUser(addUser);
        pool.setAddDatetime(new Date());
        pool.setRemark(remark);
        return stockPoolDAO.addAmount(pool);

    }

    @Override
    public int outAmount(Caigopool pool, Long usedAmount) {
        pool.setUsedAmount(pool.getUsedAmount() + usedAmount);
        return stockPoolDAO.outAmount(pool);
    }

    @Override
    public void saveCaipool(Caigopool data) {
        stockPoolDAO.insert(data);
    }

    @Override
    public Caigopool getCaigopoolByActivityCode(String activityCode) {
        Caigopool data = null;
        if (StringUtils.isNotBlank(activityCode)) {
            Caigopool condition = new Caigopool();
            condition.setActivityCode(activityCode);
            data = stockPoolDAO.select(condition);
        }
        return data;
    }

    @Override
    public Long queryCaigopoolByActivityCode(String activityCode) {
        Caigopool condition = new Caigopool();
        condition.setActivityCode(activityCode);
        return stockPoolDAO.selectTotalCount(condition);
    }

    @Override
    public void removeCaigopool(Caigopool data) {
        stockPoolDAO.delete(data);
    }
}
