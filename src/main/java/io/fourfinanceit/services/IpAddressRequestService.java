package io.fourfinanceit.services;

import javax.servlet.http.HttpServletRequest;

public interface IpAddressRequestService {

    int getRequestCount(HttpServletRequest request);

    void addIpRequest(HttpServletRequest request);

}
