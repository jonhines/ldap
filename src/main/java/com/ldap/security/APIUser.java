package com.ldap.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ldap.model.SystemCredentials;

/**
 * Customized Spring UserDetails implementation.
 * 
 * @author jon
 */
public class APIUser implements UserDetails
{

    private static final long serialVersionUID = 1L;

    private static final String ROLE_API_APPLICATION_USER = "ROLE_API_APPLICATION_USER";

    private String token;

    public APIUser()
    {
        // do nothing
    }
    
    public APIUser(SystemCredentials systemCredentials)
    {
        if(systemCredentials != null)
        {
            setToken(systemCredentials.getUserToken());
        }
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
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
        return null;
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
}
