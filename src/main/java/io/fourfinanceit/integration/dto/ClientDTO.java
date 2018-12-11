package io.fourfinanceit.integration.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientDTO {

    private Long clientId;
    private String firstName;
    private String secondName;

}
