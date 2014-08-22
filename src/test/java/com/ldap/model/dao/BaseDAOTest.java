package com.ldap.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Common configurations and utilities for integration testing.
 * 
 * @author jon
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/ldap-context.xml" })
@ActiveProfiles("local")
public abstract class BaseDAOTest
{
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    protected EntityManager getEntityManager()
    {
        return entityManager;
    }

}
