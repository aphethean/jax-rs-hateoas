/*
 * Copyright 2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayway.jaxrs.hateoas.core;

import com.sun.jersey.core.header.OutBoundHeaders;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;

/**
 * Default implementation of {@link HateoasResponse}.
 * 
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public class HateoasResponseImpl extends HateoasResponse {
    private final Response.StatusType statusType;

    private final MultivaluedMap<String, Object> headers;

    private final Object entity;

    private final Type entityType;

    /**
     * Construct given a status type, entity and metadata.
     *
     * @param statusType the status type
     * @param headers    the metadata, it is the callers responsibility to copy
     *                   the metadata if necessary.
     * @param entity     the entity
     * @param entityType the entity type, it is the callers responsibility to
     *                   ensure the entity type is compatible with the entity.
     */
    protected HateoasResponseImpl(Response.StatusType statusType, OutBoundHeaders headers, Object entity, Type entityType) {
        this.statusType = statusType;
        this.headers = headers;
        this.entity = entity;
        this.entityType = entityType;
    }

    /**
     * Construct given a status, entity and metadata.
     *
     * @param status     the status
     * @param headers    the metadata, it is the callers responsibility to copy
     *                   the metadata if necessary.
     * @param entity     the entity
     * @param entityType the entity type, it is the callers responsibility to
     *                   ensure the entity type is compatible with the entity.
     */
    protected HateoasResponseImpl(int status, OutBoundHeaders headers, Object entity, Type entityType) {
        this.statusType = toStatusType(status);
        this.headers = headers;
        this.entity = entity;
        this.entityType = entityType;
    }

    /**
     * Get the status type.
     *
     * @return the status type.
     */
    public Response.StatusType getStatusType() {
        return statusType;
    }

    /**
     * Get the entity type.
     *
     * @return the entity type.
     */
    public Type getEntityType() {
        return entityType;
    }

    // Response

    public int getStatus() {
        return statusType.getStatusCode();
    }

    public MultivaluedMap<String, Object> getMetadata() {
        return headers;
    }

    public Object getEntity() {
        return entity;
    }

    public static Response.StatusType toStatusType(final int statusCode) {
        switch (statusCode) {
            case 200:
                return Response.Status.OK;
            case 201:
                return Response.Status.CREATED;
            case 202:
                return Response.Status.ACCEPTED;
            case 204:
                return Response.Status.NO_CONTENT;

            case 301:
                return Response.Status.MOVED_PERMANENTLY;
            case 303:
                return Response.Status.SEE_OTHER;
            case 304:
                return Response.Status.NOT_MODIFIED;
            case 307:
                return Response.Status.TEMPORARY_REDIRECT;

            case 400:
                return Response.Status.BAD_REQUEST;
            case 401:
                return Response.Status.UNAUTHORIZED;
            case 403:
                return Response.Status.FORBIDDEN;
            case 404:
                return Response.Status.NOT_FOUND;
            case 406:
                return Response.Status.NOT_ACCEPTABLE;
            case 409:
                return Response.Status.CONFLICT;
            case 410:
                return Response.Status.GONE;
            case 412:
                return Response.Status.PRECONDITION_FAILED;
            case 415:
                return Response.Status.UNSUPPORTED_MEDIA_TYPE;

            case 500:
                return Response.Status.INTERNAL_SERVER_ERROR;
            case 503:
                return Response.Status.SERVICE_UNAVAILABLE;

            default: {
                return new Response.StatusType() {
                    @Override
                    public int getStatusCode() {
                        return statusCode;
                    }

                    @Override
                    public Response.Status.Family getFamily() {
                        return toFamilyCode(statusCode);
                    }

                    @Override
                    public String getReasonPhrase() {
                        return "";
                    }
                };
            }
        }
    }

    public static Response.Status.Family toFamilyCode(final int statusCode) {
        switch (statusCode / 100) {
            case 1:
                return Response.Status.Family.INFORMATIONAL;
            case 2:
                return Response.Status.Family.SUCCESSFUL;
            case 3:
                return Response.Status.Family.REDIRECTION;
            case 4:
                return Response.Status.Family.CLIENT_ERROR;
            case 5:
                return Response.Status.Family.SERVER_ERROR;
            default:
                return Response.Status.Family.OTHER;
        }
    }
}
