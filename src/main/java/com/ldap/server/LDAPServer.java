package com.ldap.server;

import javax.annotation.PostConstruct;

import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ldap.model.dao.LDAPUserDAO;

/**
 * Base class used to make initial connection to the LDAP Server as specified by properties.
 * 
 * @author jon
 * 
 */
@Component
public class LDAPServer
{
    private static final Logger logger = LoggerFactory.getLogger(LDAPServer.class);

    private LdapConnection ldapConnection;

    @Value("${ldap.server.address}")
    private String ldapServerHost;

    @Value("${ldap.server.port}")
    private int ldapServerPort;

    @Value("${ldap.bind.password}")
    private String ldapServerPassword;

    @Value("${ldap.bind.dn}")
    private String ldapServerBase;

    @PostConstruct()
    public void startServer()
    {
        ldapConnection = new LdapNetworkConnection(ldapServerHost, ldapServerPort);
        try
        {
            ldapConnection.bind(ldapServerBase, ldapServerPassword);
        }
        catch (LdapException e)
        {
            logger.error("An error occurred connecting to ldap server! Please ensure it is properly configured at: "
                    + ldapServerHost + ":" + ldapServerPort, e);
        }
    }

    public LdapConnection getLdapConnection()
    {
        return ldapConnection;
    }

    public void setLdapConnection(LdapConnection ldapConnection)
    {
        this.ldapConnection = ldapConnection;
    }

}