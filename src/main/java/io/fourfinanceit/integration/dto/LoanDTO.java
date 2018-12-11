package io.fourfinanceit.integration.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.fourfinanceit.mapping.serialization.LocalDateDeserializer;
import io.fourfinanceit.mapping.serialization.LocalDateSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public class LoanDTO {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private Long loanId;
    private Long clientId;
    private BigDecimal amount;
    private String currency;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @DateTimeFormat(pattern = DATE_PATTERN)
    private LocalDate termStart;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @DateTimeFormat(pattern = DATE_PATTERN)
    private LocalDate termEnd;

}
