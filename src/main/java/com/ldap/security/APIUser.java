package com.ldap.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ldap.dto.UserDTO;

/**
 * Customized Spring UserDetails implementation.
 * 
 * @author jon
 */
public class APIUser implements UserDetails
{

    private static final long serialVersionUID = 1L;

    private static final String ROLE_API_APPLICATION_USER = "ROLE_API_APPLICATION_USER";

    private String userName;
    private String firstName;
    private String lastName;

    public APIUser()
    {
        // do nothing
    }

    public APIUser(UserDTO userDTO)
    {
        if (userDTO != null)
        {
            setUserName(userDTO.getUserName());
            setFirstName(userDTO.getFirstName());
            setLastName(userDTO.getLastName());
        }
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities()
    {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(ROLE_API_APPLICATION_USER));
        return authorities;
    }

    @Override
    public String getPassword()
    {
        return null;
    }

    @Override
    public String getUsername()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
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
}
