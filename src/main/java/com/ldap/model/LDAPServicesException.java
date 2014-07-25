package com.ldap.model;

import javax.ws.rs.core.Response.Status;

/**
 * Unexpected exceptions for LDAP services.
 * 
 * @author jon
 * 
 */
@SuppressWarnings("serial")
public class LDAPServicesException extends RuntimeException
{

    private Status status;

    /**
     * Creates an exception with a default status of {@value Status.INTERNAL_SERVER_ERROR}
     * 
     * @param message
     */
    public LDAPServicesException(String message)
    {
        super(message);
        status = Status.INTERNAL_SERVER_ERROR;
    }

    /**
     * Creates an exception with a default status of {@value Status.INTERNAL_SERVER_ERROR}
     * 
     * @param message
     * @param cause
     */
    public LDAPServicesException(String message, Throwable cause)
    {
        super(message, cause);
        status = Status.INTERNAL_SERVER_ERROR;
    }

    /**
     * Creates an exception with a given {@link Status}, message and cause.
     * 
     * @param status
     * @param message
     */
    public LDAPServicesException(Status status, String message, Throwable cause)
    {
        super(message, cause);
        this.status = status;
    }

    /**
     * Creates an exception with a given {@link Status} and message.
     * 
     * @param status
     * @param message
     */
    public LDAPServicesException(Status status, String message)
    {
        super(message);
        this.status = status;
    }

    public Status getStatus()
    {
        return status;
    }

}
