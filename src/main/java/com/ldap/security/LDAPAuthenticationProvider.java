package com.ldap.security;
import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.ldap.model.SystemCredentials;
import com.ldap.model.dao.SystemCredentialsDAO;

/**
 * @author jon
 */
public class LDAPAuthenticationProvider implements AuthenticationProvider
{

    @Autowired
    private SystemCredentialsDAO systemCredentialsDAO;
    
    public LDAPAuthenticationProvider()
    {
        // no arg consturctor
    }

    public LDAPAuthenticationProvider(SystemCredentialsDAO systemCredentialsDAO)
    {
        this.systemCredentialsDAO = systemCredentialsDAO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.
     * core.Authentication) Provides custom authentication method
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
        String userToken = String.valueOf(auth.getPrincipal());

        if (isBlank(userToken))
        {
            throw new BadCredentialsException("API token not provided.");
        }

        SystemCredentials systemCredential = systemCredentialsDAO.getSystemCredentialsByToken(userToken);
        if (systemCredential == null)
        {
            throw new BadCredentialsException("Invalid API token provided.");
        }

        APIUser apiUser = new APIUser(systemCredential);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                apiUser, null, apiUser.getAuthorities());

        return usernamePasswordAuthenticationToken;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
     */
    @Override
    public boolean supports(Class<? extends Object> authentication)
    {
        // copied it from AbstractUserDetailsAuthenticationProvider
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
