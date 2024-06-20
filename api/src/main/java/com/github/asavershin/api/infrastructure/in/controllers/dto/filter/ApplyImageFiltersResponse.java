package com.github.asavershin.api.infrastructure.in.controllers.dto.filter;

import lombok.Getter;

@Getter
public class ApplyImageFiltersResponse {
    /**
     * UUID of the request.
     */
    private String requestId;

    /**
     * Constructs an ApplyImageFiltersResponse object with
     * the provided requestId.
     *
     * @param aRequestId The UUID of the request.
     */
    public ApplyImageFiltersResponse(final String aRequestId) {
        setRequestId(aRequestId);
    }

    private void setRequestId(final String aRequestId) {
        this.requestId = aRequestId;
    }
}
