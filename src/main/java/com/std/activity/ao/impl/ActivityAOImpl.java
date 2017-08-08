package com.std.activity.ao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.std.activity.ao.IActivityAO;
import com.std.activity.bo.IActivityBO;
import com.std.activity.bo.IActivityOrderBO;
import com.std.activity.bo.base.Paginable;
import com.std.activity.common.DateUtil;
import com.std.activity.core.OrderNoGenerater;
import com.std.activity.core.StringValidater;
import com.std.activity.domain.Activity;
import com.std.activity.dto.req.XN622010Req;
import com.std.activity.dto.req.XN622012Req;
import com.std.activity.enums.EActivityStatus;
import com.std.activity.enums.EPrefixCode;
import com.std.activity.exception.BizException;

/**
 * 
 * @author: shan 
 * @since: 2016年12月12日 上午10:43:35 
 * @history:
 */
@Service
public class ActivityAOImpl implements IActivityAO {
    static Logger logger = Logger.getLogger(ActivityAOImpl.class);

    @Autowired
    IActivityBO activityBO;

    @Autowired
    IActivityOrderBO activityOrderBO;

    @Override
    public String addNewActivity(XN622010Req req) {
        Activity data = new Activity();
        String code = OrderNoGenerater.generate(EPrefixCode.ACTIVITY.getCode());
        data.setCode(code);
        data.setTitle(req.getTitle());
        data.setPic(req.getPic());
        data.setAdvPic(req.getAdvPic());
        data.setSlogan(req.getSlogan());
        data.setAmount(StringValidater.toLong(req.getAmount()));
        data.setDescription(req.getDescription());
        data.setHoldPlace(req.getHoldPlace());
        data.setContact(req.getContact());
        data.setStartDatetime(DateUtil.strToDate(req.getStartDatetime(),
            DateUtil.DATA_TIME_PATTERN_2));
        data.setEndDatetime(DateUtil.strToDate(req.getEndDatetime(),
            DateUtil.DATA_TIME_PATTERN_2));
        data.setTotalNum(StringValidater.toInteger(req.getTotalNum()));
        data.setRemainNum(StringValidater.toInteger(req.getTotalNum()));
        data.setStatus(EActivityStatus.DRAFT.getCode());
        data.setUpdater(req.getUpdater());
        data.setUpdateDatetime(new Date());
        data.setRemark(req.getRemark());
        activityBO.saveActivity(data);
        return code;
    }

    @Override
    public void dropActivity(String code) {
        Activity activity = activityBO.getActivity(code);
        if (EActivityStatus.ONLINE.getCode().equals(activity.getStatus())
                || EActivityStatus.STOP.getCode().equals(activity.getStatus())) {
            throw new BizException("xn0000", "该活动已上线/结束,不可删除");
        }
        activityBO.deleteActivity(code);
    }

    @Override
    public void modifyActivity(XN622012Req req) {
        Activity data = activityBO.getActivity(req.getCode());
        if (EActivityStatus.STOP.getCode().equals(data.getStatus())
                || EActivityStatus.ONLINE.getCode().equals(data.getStatus())) {
            throw new BizException("xn0000", "该活动已上线/结束,不可编辑");
        }
        Integer totalNum = StringValidater.toInteger(req.getTotalNum());
        Integer number = data.getTotalNum() - data.getRemainNum();
        if (totalNum < number) {
            throw new BizException("xn0000", "当前报名人数以超过修改总人数");
        }
        Integer remainNum = totalNum - number;
        data.setTitle(req.getTitle());
        data.setPic(req.getPic());
        data.setAdvPic(req.getAdvPic());
        data.setSlogan(req.getSlogan());
        data.setAmount(StringValidater.toLong(req.getAmount()));
        data.setDescription(req.getDescription());
        data.setHoldPlace(req.getHoldPlace());
        data.setContact(req.getContact());
        data.setStartDatetime(DateUtil.strToDate(req.getStartDatetime(),
            DateUtil.DATA_TIME_PATTERN_2));
        data.setEndDatetime(DateUtil.strToDate(req.getEndDatetime(),
            DateUtil.DATA_TIME_PATTERN_2));
        data.setTotalNum(totalNum);
        data.setRemainNum(remainNum);
        data.setUpdater(req.getUpdater());
        data.setUpdateDatetime(new Date());
        data.setRemark(req.getRemark());
        activityBO.modifyActivity(data);
    }

    @Override
    public void putOn(String code, String location, String orderNo,
            String updater, String remark) {
        Activity activity = activityBO.getActivity(code);
        if (EActivityStatus.ONLINE.getCode().equals(activity.getStatus())) {
            throw new BizException("xn0000", "该活动已经上线,无需重复上线");
        }
        if (activity.getStartDatetime().before(new Date())) {
            throw new BizException("xn0000", "该活动已经结束,不可上线");
        }
        activityBO.putOn(activity, location, orderNo, updater, remark);
    }

    @Override
    public void downActivity(String code, String updater, String remark) {
        Activity activity = activityBO.getActivity(code);
        if (EActivityStatus.DRAFT.getCode().equals(activity.getStatus())
                || EActivityStatus.OFFLINE.getCode().equals(
                    activity.getStatus())
                || EActivityStatus.STOP.getCode().equals(activity.getStatus())) {
            throw new BizException("xn0000", "该活动不处于可下架状态,请核对后在操作");
        }
        activityBO.downActivity(activity, updater, remark);
    }

    @Override
    public void stopActivity(String code, String updater, String remark) {
        Activity activity = activityBO.getActivity(code);
        if (EActivityStatus.DRAFT.getCode().equals(activity.getStatus())
                || EActivityStatus.OFFLINE.getCode().equals(
                    activity.getStatus())
                || EActivityStatus.STOP.getCode().equals(activity.getStatus())) {
            throw new BizException("xn0000", "该活动未上架,不可截止。请核对后在操作");
        }
        activityBO.stopActivity(activity, updater, remark);
    }

    @Override
    public Paginable<Activity> queryActivityPage(int start, int limit,
            Activity condition) {
        return activityBO.getPaginable(start, limit, condition);
    }

    @Override
    public List<Activity> queryActivityList(Activity condition) {
        return activityBO.queryActivityList(condition);
    }

    @Override
    public Activity getActivity(String code) {
        return activityBO.getActivity(code);
    }

    @Override
    public synchronized void changeActivity() {
        logger.info("***************开始扫描待活动，过期取消***************");
        Activity condition = new Activity();
        condition.setStatus(EActivityStatus.ONLINE.getCode());
        List<Activity> activityList = activityBO.queryActivityList(condition);
        if (CollectionUtils.isNotEmpty(activityList)) {
            for (Activity activity : activityList) {
                if (activity.getStartDatetime().before(new Date())) {
                    activityBO.stopActivity(activity, "开始时间系统截止", "开始时间系统截止");
                }
            }
        }
        logger.info("***************开始扫描待活动，过期取消***************");
    }
}
