package com.ldap.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.ldap.config.LDAPCommonService;

/**
 * Base entity for all models that contains CRUD operations
 * 
 * @author jon
 */
@MappedSuperclass
public abstract class BaseModel
{
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE_TIME")
    private Date createDateTime;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_DATE_TIME")
    private Date updateDateTime;

    @Column(name = "CREATE_SOURCE", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "UPDATE_SOURCE", nullable = false, length = 50)
    private String updatedBy;

    public Date getCreateDateTime()
    {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime)
    {
        this.createDateTime = createDateTime;
    }

    public Date getUpdateDateTime()
    {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime)
    {
        this.updateDateTime = updateDateTime;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy()
    {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    public boolean isNew()
    {
        return (getCreateDateTime() == null);
    }

    @PrePersist
    @PreUpdate
    public void updateCRUD()
    {
        String createdBy = LDAPCommonService.getCurrentHost();
        Date now = LDAPCommonService.getNow();

        if (getCreateDateTime() == null)
        {
            setCreateDateTime(now);
        }

        if (getCreatedBy() == null)
        {
            setCreatedBy(createdBy);
        }

        setUpdateDateTime(now);
        setUpdatedBy(createdBy);

    }

}
