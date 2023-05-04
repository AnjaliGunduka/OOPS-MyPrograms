package com.junodx.api.models.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.ticketing.LabTicket;

import java.util.Calendar;

public class Comment {
    private Long id;
    private String commentId;
    private User commenter;
    private String comment;
    private LabTicket parentTicket;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LabTicket getParentTicket() {
        return parentTicket;
    }

    public void setParentTicket(LabTicket parentTicket) {
        this.parentTicket = parentTicket;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }
}
