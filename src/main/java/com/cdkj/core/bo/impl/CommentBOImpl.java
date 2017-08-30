package com.cdkj.core.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cdkj.core.bo.ICommentBO;
import com.cdkj.core.bo.base.PaginableBOImpl;
import com.cdkj.core.dao.ICommentDAO;
import com.cdkj.core.domain.Comment;
import com.cdkj.core.exception.BizException;

@Component
public class CommentBOImpl extends PaginableBOImpl<Comment> implements
        ICommentBO {

    @Autowired
    private ICommentDAO commentDAO;

    @Override
    public boolean isCommentExist(String code) {
        Comment condition = new Comment();
        condition.setCode(code);
        if (commentDAO.selectTotalCount(condition) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void saveComment(Comment data) {
        commentDAO.insert(data);
    }

    @Override
    public void removeComment(String code) {
        if (StringUtils.isNotBlank(code)) {
            Comment data = new Comment();
            data.setCode(code);
            commentDAO.delete(data);
        }
    }

    @Override
    public void approveComment(Comment data) {
        if (StringUtils.isNotBlank(data.getCode())) {
            commentDAO.update(data);
        }
    }

    @Override
    public List<Comment> queryCommentList(Comment condition) {
        return commentDAO.selectList(condition);
    }

    @Override
    public Comment getComment(String code) {
        Comment data = null;
        if (StringUtils.isNotBlank(code)) {
            Comment condition = new Comment();
            condition.setCode(code);
            data = commentDAO.select(condition);
            if (data == null) {
                throw new BizException("xn0000", "编号不存在");
            }
        }
        return data;
    }

    @Override
    public void approveComment(Comment data, String status, String approver,
            String remark) {
        data.setStatus(status);
        data.setApprover(approver);
        data.setApproveDatetime(new Date());
        data.setRemark(remark);
        commentDAO.approveComment(data);
    }

}
