package com.cdkj.core.ao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cdkj.core.ao.ICommentAO;
import com.cdkj.core.bo.ICommentBO;
import com.cdkj.core.bo.IKeywordBO;
import com.cdkj.core.bo.IUserBO;
import com.cdkj.core.bo.base.Page;
import com.cdkj.core.bo.base.Paginable;
import com.cdkj.core.core.OrderNoGenerater;
import com.cdkj.core.domain.Comment;
import com.cdkj.core.domain.User;
import com.cdkj.core.dto.req.XN003010CReq;
import com.cdkj.core.dto.req.XN003010Req;
import com.cdkj.core.dto.res.XN801028Res;
import com.cdkj.core.enums.EBoolean;
import com.cdkj.core.enums.ECommentStatus;
import com.cdkj.core.enums.ECommentType;
import com.cdkj.core.enums.EPrefixCode;
import com.cdkj.core.enums.EReaction;
import com.cdkj.core.exception.BizException;

@Service
public class CommentAOImpl implements ICommentAO {

    @Autowired
    private ICommentBO commentBO;

    @Autowired
    private IKeywordBO keywordBO;

    @Autowired
    private IUserBO userBO;

    @Override
    @Transactional
    public List<String> comment(XN003010Req req) {
        List<String> result = new ArrayList<String>();
        for (XN003010CReq cReq : req.getCommentList()) {
            String code = comment(req.getType(), req.getOrderCode(), cReq,
                req.getCommenter(), req.getCommenterName(),
                req.getCompanyCode(), req.getSystemCode());
            result.add(code);
        }
        return result;
    }

    private String comment(String type, String orderCode, XN003010CReq comment,
            String commenter, String commenterName, String companyCode,
            String systemCode) {
        // 判断是否含有关键字
        EReaction result = keywordBO.checkContent(comment.getContent());
        String code = OrderNoGenerater
            .generateME(EPrefixCode.COMMENT.getCode());
        Comment data = new Comment();
        data.setCode(code);
        data.setType(type);
        data.setOrderCode(orderCode);
        data.setScore(Integer.valueOf(comment.getScore()));
        data.setContent(comment.getContent());

        String status = ECommentStatus.PUBLISHED.getCode();
        if (EReaction.REFUSE.getCode().equals(result.getCode())) {
            status = ECommentStatus.FILTERED.getCode();
            code = code + "||" + comment.getEntityCode() + "||" + "||filter";
        }
        data.setStatus(status);

        data.setCommenter(commenter);
        data.setCommenterName(commenterName);
        data.setCommentDatetime(new Date());
        data.setParentCode(comment.getParentCode());
        data.setEntityCode(comment.getEntityCode());

        data.setEntityName(comment.getEntityName());
        data.setCompanyCode(companyCode);
        data.setSystemCode(systemCode);
        commentBO.saveComment(data);
        return code;
    }

    @Override
    public void comment(String entityCode, String type, String userId,
            String content, String parentCode, String companyCode,
            String systemCode) {
        ECommentType eCommentType = ECommentType.getResultMap().get(type);
        commentBO.saveComment(userId, eCommentType, content, parentCode,
            entityCode, companyCode, systemCode);
    }

    @Override
    public void approveComment(String code, String result, String approver,
            String remark) {
        Comment data = commentBO.getComment(code);
        if (!ECommentStatus.FILTERED.getCode().equals(data.getStatus())) {
            throw new BizException("xn0000", "该评论不是待审核状态,不能审核");
        }
        String status = ECommentStatus.APPROVE_YES.getCode();
        if (EBoolean.NO.getCode().equals(result)) {
            status = ECommentStatus.APPROVE_NO.getCode();
        }
        commentBO.approveComment(data, status, approver, remark);
    }

    @Override
    public void dropComment(String code) {
        Comment comment = commentBO.getComment(code);
        commentBO.removeComment(comment);
    }

    @Override
    public Paginable<Comment> queryCommentPage(int start, int limit,
            Comment condition) {
        Paginable<Comment> page = commentBO.getPaginable(start, limit,
            condition);
        List<Comment> commentList = page.getList();
        for (Comment comment : commentList) {
            User user = userBO.getRemoteUser(comment.getCommenter());
            comment.setNickname(user.getNickname());
            comment.setPhoto(user.getPhoto());
        }
        return page;
    }

    @Override
    public XN801028Res queryFrontScoreCommentPage(int start, int limit,
            Comment condition) {
        Page<Comment> page = (Page<Comment>) commentBO.getPaginable(start,
            limit, condition);
        List<Comment> commentList = page.getList();
        for (Comment comment : commentList) {
            User user = userBO.getRemoteUser(comment.getCommenter());
            comment.setNickname(user.getNickname());
            comment.setPhoto(user.getPhoto());
        }
        Double totalScore = commentBO.queryTotalScore(condition) / 5.0;// 5颗星好评
        Long totalCount = commentBO.getTotalCount(condition);
        double avgScore = 0d;
        if (totalCount.longValue() != 0) {
            avgScore = (totalScore) / totalCount;
        }
        return new XN801028Res(page, avgScore);
    }

    @Override
    public Paginable<Comment> queryFrontCommentPage(int start, int limit,
            Comment condition) {
        Paginable<Comment> page = commentBO.getPaginable(start, limit,
            condition);
        List<Comment> commentList = page.getList();
        if (!CollectionUtils.isEmpty(commentList)) {
            for (Comment comment : commentList) {
                commentBO.getRichComment(comment);
            }
        }
        return page;
    }

    @Override
    public Paginable<Comment> queryFrontOneCommentPage(int start, int limit,
            Comment condition) {
        Paginable<Comment> page = commentBO.getPaginable(start, limit,
            condition);
        List<Comment> commentList = page.getList();
        if (!CollectionUtils.isEmpty(commentList)) {
            for (Comment comment : commentList) {
                commentBO.getNextComment(comment);
            }
        }
        return page;
    }

    @Override
    public Comment getComment(String code) {
        Comment comment = commentBO.getComment(code);
        User user = userBO.getRemoteUser(comment.getCommenter());
        comment.setNickname(user.getNickname());
        comment.setPhoto(user.getPhoto());
        return comment;
    }
}
