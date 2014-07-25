package com.ldap.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ldap.model.SystemCredentials;

/**
 * 
 * CRUD operations for {@link SystemCredentials}s.
 * 
 * @author jon
 * 
 */
@Repository
public class SystemCredentialsDAO
{
    private static final Logger logger = LoggerFactory.getLogger(SystemCredentialsDAO.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(SystemCredentials systemCredentials)
    {
        logger.debug("Saving SystemCredentials: {}", systemCredentials);

        if (systemCredentials.isNew())
        {
            entityManager.persist(systemCredentials);
        }
        else
        {
            entityManager.merge(systemCredentials);
        }
    }

    /**
     * Fetch a single credentials based on username.
     * 
     * @param userName
     * @return {@link SystemCredentials}
     */
    public SystemCredentials getSystemCredentialsByUsername(String userName)
    {
        Validate.notNull(userName, "userName is required");

        String jql = "from " + SystemCredentials.class.getName() + " cred where cred.userName = :userName";
        TypedQuery<SystemCredentials> credentialTypeQuery = entityManager.createQuery(jql, SystemCredentials.class);
        credentialTypeQuery.setParameter("userName", userName);

        SystemCredentials returnCredentials = null;

        try
        {
            returnCredentials = credentialTypeQuery.getSingleResult();
        }
        catch (NoResultException e)
        {
            // do nothing
        }

        return returnCredentials;
    }

    /**
     * Fetches a single credentials based on token; also caches the query in second level cache
     * 
     * @param token
     * @return {@link SystemCredentials}
     */
    public SystemCredentials getSystemCredentialsByToken(String token)
    {
        Validate.notNull(token, "token is required");

        String jql = "from " + SystemCredentials.class.getName() + " cred where cred.userToken = :token";
        TypedQuery<SystemCredentials> credentialTypeQuery = entityManager.createQuery(jql, SystemCredentials.class);
        credentialTypeQuery.setParameter("token", token);
        credentialTypeQuery.setHint("org.hibernate.cacheable", true);
        SystemCredentials returnCredentials = null;

        try
        {
            returnCredentials = credentialTypeQuery.getSingleResult();
        }
        catch (NoResultException e)
        {
            // do nothing
        }

        return returnCredentials;
    }
}
