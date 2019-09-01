package com.mariner.xvu.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Value is stored in this bean after successful
 * parsing of csv, JSON and XML
 *
 * @author  Ankit Mayur
 * @version 1.0
 * @since   2019-08-31
 */

public class Report {

    private String clientAddress;
    private String clientGuid;
    private String requestTime;
    private String serviceGuid;
    private String retriesRequest;
    private String packetsRequested;
    private String packetsServiced;
    private String maxHoleSize;

    public Report() {
        //do nothing
    }

    @JsonProperty("client-address")
    public String getClientAddress() {
        return clientAddress;
    }
    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    @JsonProperty("client-guid")
    public String getClientGuid() {
        return clientGuid;
    }
    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    @JsonProperty("request-time")
    public String getRequestTime() {
        return requestTime;
    }
    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    @JsonProperty("service-guid")
    public String getServiceGuid() {
        return serviceGuid;
    }
    public void setServiceGuid(String serviceGuid) {
        this.serviceGuid = serviceGuid;
    }

    @JsonProperty("retries-request")
    public String getRetriesRequest() {
        return retriesRequest;
    }
    public void setRetriesRequest(String retriesRequest) {
        this.retriesRequest = retriesRequest;
    }

    @JsonProperty("packets-requested")
    public String getPacketsRequested() {
        return packetsRequested;
    }
    public void setPacketsRequested(String packetsRequested) {
        this.packetsRequested = packetsRequested;
    }

    @JsonProperty("packets-serviced")
    public String getPacketsServiced() {
        return packetsServiced;
    }
    public void setPacketsServiced(String packetsServiced) {
        this.packetsServiced = packetsServiced;
    }

    @JsonProperty("max-hole-size")
    public String getMaxHoleSize() {
        return maxHoleSize;
    }
    public void setMaxHoleSize(String maxHoleSize) {
        this.maxHoleSize = maxHoleSize;
    }
}
