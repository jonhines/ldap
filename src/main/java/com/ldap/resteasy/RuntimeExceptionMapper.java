package com.ldap.resteasy;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ExceptionMapper} for {@link RuntimeException}s
 * 
 * @author jon
 * 
 */
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException>
{

    private static final Logger logger = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

    @Override
    public Response toResponse(RuntimeException rex)
    {
        logger.error("Runtime exception encountered", rex);

        Status resultStatus = Status.INTERNAL_SERVER_ERROR;
        String message = rex.getMessage();

        return Response.status(resultStatus).entity(message).build();
    }

}
