package com.ldap.model.dao;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.List;

import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ldap.constants.LDAPConstants;
import com.ldap.dto.UserDTO;
import com.ldap.model.LDAPServicesException;

/**
 * DAO for executing search requests that relate to Users within the LDAP server.
 * 
 * @author jon
 * 
 */
@Service
public class LDAPUserDAO extends LDAPDAO
{
    private static final Logger logger = LoggerFactory.getLogger(LDAPUserDAO.class);

    @Value("${ldap.userlookup.base.dn}")
    private String ldapUserSearchDomain;

    protected LDAPUserDAO()
    {
        // nothing to do here, for spring.
    }

    /**
     * Execute a search for against the "Users" organizationalUnit in the LDAP for users that match the given
     * searchTerm.
     * 
     * @param searchTerm
     * @return {@link UserDTO}s
     * @throws LdapException
     * @throws CursorException
     */
    public List<UserDTO> searchUsers(String searchTerm)
    {
        List<UserDTO> searchResults = new ArrayList<UserDTO>();

        SearchRequest searchRequest = buildUserSearchRequest(searchTerm);

        try
        {
            SearchCursor cursor = getLDAPConnection().search(searchRequest);

            if (cursor == null)
            {
                return searchResults;
            }

            while (cursor.next())
            {
                Entry resultEntry = cursor.getEntry();

                UserDTO searchResult = transformUserEntryToUserDTO(resultEntry);

                searchResults.add(searchResult);
            }
        }
        catch (Exception e)
        {
            logger.error("An error occurred searching LDAP users by searchTerm: " + searchTerm, e);
        }

        return searchResults;
    }

    /**
     * Execute a search against the LDAP server for an exact match of the provided userName.
     * 
     * @param userName
     * @return
     * @throws LdapException
     * @throws CursorException
     */
    public UserDTO getUser(String userName)
    {
        UserDTO userResultDTO = new UserDTO();
        SearchRequest searchRequest = buildUserNameRequest(userName);
        try
        {
            SearchCursor cursor = getLDAPConnection().search(searchRequest);
            if (cursor == null)
            {
                return userResultDTO;
            }
            while (cursor.next())
            {
                Entry resultEntry = cursor.getEntry();

                userResultDTO = transformUserEntryToUserDTO(resultEntry);
            }
        }
        catch (Exception e)
        {
            logger.error("An error occurred fetching user by userName: " + userName, e);
        }

        return userResultDTO;
    }

    /**
     * Execute a search against the LDAP server for an exact match of the provided userName and password.
     * 
     * If no search result is found, then null is returned.
     * 
     * @param userName
     * @param password
     * @return
     */
    public UserDTO getUserForAuthentication(String userName, String password)
    {
        if (isBlank(userName))
        {
            throw new LDAPServicesException("userName cannot be null.");
        }
        if (isBlank(password))
        {
            throw new LDAPServicesException("password cannot be null.");
        }

        SearchRequest searchRequest = buildUserAuthenticationRequest(userName, password);
        try
        {
            SearchCursor cursor = getLDAPConnection().search(searchRequest);
            if (cursor == null)
            {
                return null;
            }
            while (cursor.next())
            {
                Entry resultEntry = cursor.getEntry();

                UserDTO userResultDTO = transformUserEntryToUserDTO(resultEntry);

                return userResultDTO;
            }
        }
        catch (Exception e)
        {
            String errorMessage = "An error occurred fetching user by userName: " + userName;
            logger.error(errorMessage, e);
            throw new LDAPServicesException(errorMessage, e);
        }
        return null;
    }

    /**
     * Convert a {@link Entry} into a {@link UserDTO} for consumption by client.
     * 
     * @param resultEntry
     * @return {@link UserDTO}
     */
    protected UserDTO transformUserEntryToUserDTO(Entry resultEntry)
    {
        UserDTO searchResult = new UserDTO();
        try
        {
            Attribute lastName = resultEntry.get(LDAPConstants.LDAP_ATTR_LASTNAME);
            Attribute firstName = resultEntry.get(LDAPConstants.LDAP_ATTR_FIRSTNAME);
            Attribute userName = resultEntry.get(LDAPConstants.LDAP_ATTR_USERNAME);

            if (firstName != null)
            {
                searchResult.setFirstName(firstName.getString());
            }

            if (lastName != null)
            {
                searchResult.setLastName(lastName.getString());
            }

            if (userName != null)
            {
                searchResult.setUserName(userName.getString());
            }

        }
        catch (Exception e)
        {
            String errorMessage = "An error occurred transforming User Entry to DTO.";
            logger.error(errorMessage, e);
            throw new LDAPServicesException(errorMessage, e);
        }

        return searchResult;
    }

    /**
     * Build a {@link SearchRequest} object to be used in a User search in the LDAP server. Wildcards are added to the
     * searchTerm so that non-exact matching can be done.
     * 
     * @param searchTerm
     * @return
     * @throws LdapException
     */
    private SearchRequest buildUserSearchRequest(String searchTerm)
    {
        SearchRequest searchRequest = buildBaseSearchRequest(ldapUserSearchDomain);
        try
        {
            searchRequest.setFilter(buildLDAPFilter(LDAPConstants.LDAP_ATTR_FULLNAME, searchTerm, false));
        }
        catch (LdapException e)
        {
            logger.error("An error occurred building search filter for LDAP user search", e);
        }

        return searchRequest;
    }

    /**
     * Build a {@link SearchRequest} object to be used in a User search in the LDAP server that searches only username
     * 
     * @param userName
     * @return
     * @throws LdapException
     */
    private SearchRequest buildUserNameRequest(String userName)
    {
        SearchRequest searchRequest = buildBaseSearchRequest(ldapUserSearchDomain);
        try
        {
            searchRequest.setFilter(buildLDAPFilter(LDAPConstants.LDAP_ATTR_USERNAME, userName, true));
        }
        catch (LdapException e)
        {
            logger.error("An error occurred building search filter for LDAP user name search", e);
        }

        return searchRequest;
    }

    /**
     * Build a {@link SearchRequest} object to be used in a User search in the LDAP server that searches only username
     * and password exact matches.
     * 
     * @param userName
     * @param password - assuming it has already been hashed and salted and prepended with the algorithm type
     * @return
     */
    private SearchRequest buildUserAuthenticationRequest(String userName, String password)
    {
        SearchRequest searchRequest = buildBaseSearchRequest(ldapUserSearchDomain);
        try
        {
            // this filter is creates a filter for both username and password. the "&" at the beginning tells us to only
            // return results that match BOTH filters.
            searchRequest.setFilter("(&" + buildLDAPFilter(LDAPConstants.LDAP_ATTR_USERNAME, userName, true)
                    + buildLDAPFilter(LDAPConstants.LDAP_ATTR_PASSWORD, password, true) + ")");
        }
        catch (LdapException e)
        {
            logger.error("An error occurred building search filter for LDAP user name search", e);
        }

        return searchRequest;
    }
}
