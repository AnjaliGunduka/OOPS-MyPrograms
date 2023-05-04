package com.junodx.api.models.ticketing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.core.Comment;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.ticketing.types.TicketType;

import java.util.Calendar;
import java.util.List;

public class LabTicket {
    @JsonIgnore
    private Long id;

    private String ticketId;
    private String subject;
    private String body;
    private User recipient;
    private User sender;
    private User currentOwner;
    private LaboratoryOrder laboratoryOrder;
    private Calendar createAt;
    private List<Comment> comment;
    private TicketType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(User currentOwner) {
        this.currentOwner = currentOwner;
    }

    public LaboratoryOrder getLaboratoryOrder() {
        return laboratoryOrder;
    }

    public void setLaboratoryOrder(LaboratoryOrder laboratoryOrder) {
        this.laboratoryOrder = laboratoryOrder;
    }

    public Calendar getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Calendar createAt) {
        this.createAt = createAt;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }
}
