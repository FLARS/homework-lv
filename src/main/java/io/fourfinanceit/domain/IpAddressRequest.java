package io.fourfinanceit.domain;

import lombok.Data;
import springfox.documentation.annotations.Cacheable;

@Data
public class IpAddressRequest {

    private String ipAddress;
    private int counter;

    public synchronized void incrementCounter() {
        counter++;
    }
}
