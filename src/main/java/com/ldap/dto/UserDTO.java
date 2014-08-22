package com.ldap.dto;

/**
 * DTO for a a search result for a user that exists in the LDAP server.
 * 
 * @author jon
 * 
 */
public class UserDTO
{
    private String firstName;

    private String lastName;

    private String userName;

    private String userPassword;

    public UserDTO()
    {
        // do nothing here
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserPassword()
    {
        return userPassword;
    }

    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }

}
