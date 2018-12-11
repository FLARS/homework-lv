package io.fourfinanceit.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientDTO {

    private Long clientId;
    private String firstName;
    private String secondName;

}
