package com.ldap.businessservice;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ldap.config.LDAPCommonService;
import com.ldap.dto.SystemCredentialsDTO;
import com.ldap.model.LDAPServicesException;
import com.ldap.model.SystemCredentials;
import com.ldap.model.dao.SystemCredentialsDAO;

/**
 * "Business" logic related to managing {@link SystemCredentials}s.
 * 
 * @author jon
 * 
 */
@Service
public class SystemCredentialsBusinessServiceImpl
{
    private static final Logger logger = LoggerFactory.getLogger(SystemCredentialsBusinessServiceImpl.class);

    @Autowired
    private SystemCredentialsDAO systemCredentialsDAO;

    protected SystemCredentialsBusinessServiceImpl()
    {
        // nothing to do here, for spring.
    }

    public SystemCredentialsBusinessServiceImpl(SystemCredentialsDAO systemCredentialsDAO)
    {
        this.systemCredentialsDAO = systemCredentialsDAO;
    }

    /**
     * Create a given {@link SystemCredentials}
     * 
     * Throws error if a credential exists with same userName.
     * 
     * @param systemCredentialsDTO
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SystemCredentials createSystemCredentials(SystemCredentialsDTO systemCredentialsDTO)
    {
        logger.debug("Creating new system credentials");

        if (systemCredentialsDTO == null)
        {
            throw new LDAPServicesException("DTO cannot be null");
        }

        if (isBlank(systemCredentialsDTO.getUserName()))
        {
            throw new LDAPServicesException("A User Name must be specified.");
        }

        SystemCredentials existingCredential = systemCredentialsDAO.getSystemCredentialsByUsername(systemCredentialsDTO
                .getUserName());
        if (existingCredential != null)
        {
            logger.error("Attempting to create credentials for existing user: {}", systemCredentialsDTO.getUserName());
            throw new LDAPServicesException("Credentials exist for this user already.");
        }

        SystemCredentials systemCredentials = new SystemCredentials();

        systemCredentials.setUserName(systemCredentialsDTO.getUserName());

        generateSystemCredentialToken(systemCredentials);

        getSystemCredentialsDao().save(systemCredentials);

        return systemCredentials;
    }

    /**
     * Generate a new userToken for a given {@link SystemCredentials}
     * 
     * @param systemCredentials
     */
    protected void generateSystemCredentialToken(SystemCredentials systemCredentials)
    {
        if (systemCredentials == null)
        {
            return;
        }

        String token = LDAPCommonService.getUuidNoDashes();
        systemCredentials.setUserToken(token);
    }

    protected SystemCredentialsDAO getSystemCredentialsDao()
    {
        return systemCredentialsDAO;
    }

    protected void setSystemCredentialsDao(SystemCredentialsDAO systemCredentialsDAO)
    {
        this.systemCredentialsDAO = systemCredentialsDAO;
    }

}
