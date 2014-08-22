package com.ldap.security;

import static org.apache.commons.lang.StringUtils.isBlank;

import org.apache.commons.codec.binary.Base64;
import org.apache.directory.api.ldap.model.constants.LdapSecurityConstants;
import org.apache.directory.api.ldap.model.password.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.ldap.dto.UserDTO;
import com.ldap.model.dao.LDAPUserDAO;

/**
 * @author jon
 */
public class LDAPAuthenticationProvider implements AuthenticationProvider
{
    @Autowired
    private LDAPUserDAO ldapUserDAO;

    public LDAPAuthenticationProvider()
    {
        // no arg consturctor
    }

    public LDAPAuthenticationProvider(LDAPUserDAO ldapUserDAO)
    {
        this.ldapUserDAO = ldapUserDAO;
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
        String userName = String.valueOf(auth.getPrincipal());
        String userPassword = String.valueOf(auth.getCredentials());

        if (isBlank(userName))
        {
            throw new BadCredentialsException("Username not provided.");
        }

        if (isBlank(userPassword))
        {
            throw new BadCredentialsException("Password not provided.");
        }

        UserDTO authenticatedUserDTO = ldapUserDAO.getUserForAuthentication(userName,
                preparePasswordForRequest(userPassword));
        if (authenticatedUserDTO == null)
        {
            throw new BadCredentialsException("Invalid credentials provided.");
        }

        APIUser apiUser = new APIUser(authenticatedUserDTO);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                apiUser, null, apiUser.getAuthorities());

        return usernamePasswordAuthenticationToken;
    }

    /**
     * Hash password based ont he default hashing algorithm used in all passwords that exist in the LDAP.
     * @param userPassword
     * @return hashed password prepended with algorithm name.
     */
    private String preparePasswordForRequest(String userPassword)
    {
        byte[] hashedPassword = PasswordUtil.encryptPassword(userPassword.getBytes(),
                LdapSecurityConstants.HASH_METHOD_SHA, null);
        String saltedPassword = new String(Base64.encodeBase64(hashedPassword));
        saltedPassword = "{" + LdapSecurityConstants.HASH_METHOD_SHA.getPrefix() + "}" + saltedPassword;

        return saltedPassword;
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
