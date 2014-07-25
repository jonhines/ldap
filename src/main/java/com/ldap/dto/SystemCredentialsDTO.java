package com.ldap.dto;

import org.codehaus.jackson.annotate.JsonProperty;

import com.ldap.model.SystemCredentials;

/**
 * DTO for a restful system credential.
 * 
 * @author jon
 * 
 */
public class SystemCredentialsDTO
{
    private String systemCredentialsId;

    @JsonProperty("username")
    private String userName;

    @JsonProperty("token")
    private String userToken;

    public SystemCredentialsDTO()
    {
        // do nothing here
    }

    public SystemCredentialsDTO(SystemCredentials systemCredentials)
    {
        if (systemCredentials != null)
        {
            setUserName(systemCredentials.getUserName());
            setUserToken(systemCredentials.getUserToken());
        }
    }

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
