package com.ofg.infrastructure.web.resttemplate.fluent.common.response.executor

import groovy.transform.TypeChecked
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestOperations

import static com.ofg.infrastructure.web.resttemplate.fluent.common.response.executor.HttpEntityUtils.getHttpEntityFrom
import static com.ofg.infrastructure.web.resttemplate.fluent.common.response.executor.UrlParsingUtils.appendPathToHost

@TypeChecked
abstract class LocationFindingExecutor implements LocationReceiving {

    protected final Map params = [:]
    protected final RestOperations restOperations

    LocationFindingExecutor(RestOperations restOperations) {
        this.restOperations = restOperations
    }

    protected abstract HttpMethod getHttpMethod()

    @Override
    URI forLocation() {
        if(params.url) {
            return restOperations.exchange(new URI(appendPathToHost(params.host as String, params.url as URI)), getHttpMethod(), getHttpEntityFrom(params), params.request.class)?.headers?.getLocation()
        } else if(params.urlTemplate) {
            return restOperations.exchange(appendPathToHost(params.host as String, params.urlTemplate as String), getHttpMethod(), getHttpEntityFrom(params), params.request.class, params.urlVariablesArray as Object[] ?: params.urlVariablesMap as Map<String, ?>)?.headers?.getLocation()
        }
        throw new InvalidHttpMethodParametersException(params)
    }
}