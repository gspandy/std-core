/**
 * @Title PostTalkBOImpl.java 
 * @Package com.std.forum.bo.impl 
 * @Description 
 * @author xieyj  
 * @date 2016年8月29日 下午5:07:00 
 * @version V1.0   
 */
package com.std.forum.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.std.forum.bo.IPostTalkBO;
import com.std.forum.bo.base.PaginableBOImpl;
import com.std.forum.core.OrderNoGenerater;
import com.std.forum.dao.IPostTalkDAO;
import com.std.forum.domain.PostTalk;
import com.std.forum.enums.EPrefixCode;

/** 
 * @author: xieyj 
 * @since: 2016年8月29日 下午5:07:00 
 * @history:
 */
@Component
public class PostTalkBOImpl extends PaginableBOImpl<PostTalk> implements
        IPostTalkBO {
    @Autowired
    private IPostTalkDAO postTalkDAO;

    /** 
     * @see com.std.forum.bo.IPostTalkBO#savePostTalk(com.std.forum.domain.PostTalk)
     */
    @Override
    public int savePostTalk(PostTalk data) {
        int count = 0;
        if (data != null) {
            String code = OrderNoGenerater.generate(EPrefixCode.POSTTALK
                .getCode());
            data.setCode(code);
            data.setTalkDatetime(new Date());
            count = postTalkDAO.insert(data);
        }
        return count;
    }

    /** 
     * @see com.std.forum.bo.IPostTalkBO#queryPostTalkList(com.std.forum.domain.PostTalk)
     */
    @Override
    public List<PostTalk> queryPostTalkSingleList(String postCode, String type) {
        List<PostTalk> resultList = null;
        if (StringUtils.isNotBlank(postCode) && StringUtils.isNotBlank(type)) {
            PostTalk condition = new PostTalk();
            condition.setPostCode(postCode);
            condition.setType(type);
            resultList = postTalkDAO.selectList(condition);
        }
        return resultList;
    }

    /** 
     * @see com.std.forum.bo.IPostTalkBO#queryPostTalkList(com.std.forum.domain.PostTalk)
     */
    @Override
    public List<PostTalk> queryPostTalkList(PostTalk condition) {
        return postTalkDAO.selectList(condition);
    }

    /** 
     * @see com.std.forum.bo.IPostTalkBO#getPostTalk(java.lang.String)
     */
    @Override
    public PostTalk getPostTalk(PostTalk data) {
        PostTalk result = null;
        if (StringUtils.isNotBlank(data.getPostCode())
                && StringUtils.isNotBlank(data.getTalker())) {
            PostTalk condition = new PostTalk();
            condition.setType(data.getType());
            condition.setPostCode(data.getPostCode());
            condition.setTalker(data.getTalker());
            result = postTalkDAO.select(condition);
        }
        return result;
    }

    @Override
    public int removePostTalk(String code) {
        int count = 0;
        if (null != code) {
            PostTalk data = new PostTalk();
            data.setCode(code);
            count = postTalkDAO.delete(data);
        }
        return count;
    }

    @Override
    public long getPersonCount(PostTalk condition) {
        long count = 0;
        if (null != condition.getPlateCode()) {
            count = postTalkDAO.selectPersonCount(condition);
        }
        return count;
    }
}