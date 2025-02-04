package com.kinder.kinder_ielts.entity.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
public abstract class BaseEntity {
    @NotNull
    @Column(name = "create_time", nullable = true, updatable = false)
    private ZonedDateTime createTime;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by", referencedColumnName = "id", nullable = true, updatable = false)
    private Account createBy;

    @Column(name = "modify_time")
    private ZonedDateTime modifyTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modify_by", referencedColumnName = "id")
    private Account modifyBy;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "is_deleted", nullable = true)
    @Basic(fetch = FetchType.EAGER)
    private IsDelete isDeleted = IsDelete.NOT_DELETED;

    public void initForNew(){
        this.createTime = ZonedDateTime.now();
        this.createBy = SecurityContextHolderUtil.getAccount();
        this.isDeleted = IsDelete.NOT_DELETED;
    }

    public void initForNew(Account account, ZonedDateTime currentTime, IsDelete isDeleted){
        this.createTime = currentTime;
        this.createBy = account;
        this.isDeleted = isDeleted;
    }

    public void initForNew(Account account, ZonedDateTime currentTime){
        this.createTime = currentTime;
        this.createBy = account;
    }

    public void updateAudit(Account modifier, ZonedDateTime modifyTime){
        this.modifyBy = modifier;
        this.modifyTime = modifyTime;
    }
}
