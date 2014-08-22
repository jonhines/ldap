package com.ldap.model.dao;

import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldap.constants.LDAPConstants;
import com.ldap.server.LDAPServer;

/**
 * DAO for executing search requests that relate to Users within the LDAP server.
 * 
 * @author jon
 * 
 */
@Service
public class LDAPDAO
{
    private static final Logger logger = LoggerFactory.getLogger(LDAPDAO.class);

    @Autowired
    private LDAPServer ldapServer;

    /**
     * Build a base {@link SearchRequest} object to be used in conjunction with other searches.
     * 
     * @param ldapDomain - from which to execute the search against.
     * @return
     */
    protected SearchRequest buildBaseSearchRequest(String ldapDomain)
    {
        SearchRequest searchRequest = new SearchRequestImpl();
        try
        {
            searchRequest.setBase(new Dn(ldapDomain));
        }
        catch (LdapInvalidDnException e)
        {
            logger.error("An error occurred applying domain to LDAP search for: " + ldapDomain, e);
        }
        searchRequest.setScope(SearchScope.SUBTREE);
        searchRequest.addAttributes(LDAPConstants.ALL_LDAP_OBJECT_PROPERTIES);

        return searchRequest;
    }

    /**
     * Build a search filter string that is for a single attribute and search term. This filter applies wildcards to the
     * search term.
     * 
     * @param attribute
     * @param searchTerm
     * @param exactMatch - if true, then search results will only return exact matches to the search term.
     * @return
     */
    protected String buildLDAPFilter(String attribute, Object searchTerm, boolean exactMatch)
    {
        if (exactMatch)
        {
            return "(" + attribute + "=" + searchTerm + ")";
        }

        return "(" + attribute + "=" + applySearchWildCard(searchTerm) + ")";
    }

    /**
     * This method prepends and appends the search wildcard to the searchTerm so that exact match is not required.
     * 
     * @param searchTerm
     * @return
     */
    private String applySearchWildCard(Object searchTerm)
    {
        return LDAPConstants.LDAP_SEARCH_WILDCARD + searchTerm + LDAPConstants.LDAP_SEARCH_WILDCARD;
    }

    public LdapConnection getLDAPConnection()
    {
        return ldapServer.getLdapConnection();
    }

}
