package io.fourfinanceit.services;

import io.fourfinanceit.domain.IpAddressRequest;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.StringTokenizer;

@Service
@AllArgsConstructor
@CacheConfig(cacheNames = {"ipAddressRequests"})
public class IpAddressRequestServiceImpl implements IpAddressRequestService {

    private final CacheManager cacheManager;

    private static final int INIT_COUNTER = 1;

    public int getRequestCount(HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        return cacheManager
                .getCache("ipAddressRequests")
                .get(ipAddress, IpAddressRequest.class)
                .getCounter();
    }

    public void addIpRequest(HttpServletRequest request) {
        String clientIpAddress = getClientIpAddress(request);
        IpAddressRequest ipAddressFromCache = cacheManager
                .getCache("ipAddressRequests")
                .get(clientIpAddress, IpAddressRequest.class);
        if (ipAddressFromCache != null) {
            ipAddressFromCache.incrementCounter();
            cacheManager.getCache("ipAddressRequests").put(clientIpAddress, ipAddressFromCache);
        } else {
            IpAddressRequest ipAddressRequest = new IpAddressRequest();
            ipAddressRequest.setIpAddress(clientIpAddress);
            ipAddressRequest.setCounter(INIT_COUNTER);
            cacheManager.getCache("ipAddressRequests").put(clientIpAddress, ipAddressRequest);
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }
}
