package com.ldap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


/**
 * Represents a user of our system in some way. This model holds the credentials for each possible user of our system.
 * 
 * @author jon
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "TBL_SYSTEM_CREDENTIALS")
public class SystemCredentials extends BaseModel
{

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "SYSTEM_CREDENTIALS_ID")
    private String systemCredentialsId;

    @Column(name = "USER_NAME", nullable = false, length = 50)
    private String userName;

    @Column(name = "USER_TOKEN", nullable = false, length = 200)
    private String userToken;

    public String getSystemCredentialsId()
    {
        return systemCredentialsId;
    }

    public void setSystemCredentialsId(String systemCredentialsId)
    {
        this.systemCredentialsId = systemCredentialsId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserToken()
    {
        return userToken;
    }

    public void setUserToken(String userToken)
    {
        this.userToken = userToken;
    }

}
