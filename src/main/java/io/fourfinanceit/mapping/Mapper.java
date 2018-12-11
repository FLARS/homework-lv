package io.fourfinanceit.mapping;

import io.fourfinanceit.rest.dto.LoanDTO;
import io.fourfinanceit.domain.Client;
import io.fourfinanceit.domain.Loan;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class Mapper {

    private final MapperFacade mapperFacade;

    public Mapper() {
        DefaultMapperFactory factory = new DefaultMapperFactory.Builder().build();
        factory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDate.class));
        ClassMapBuilder<LoanDTO, Loan> classMapBuilder = factory.classMap(LoanDTO.class, Loan.class);
        classMapBuilder
                .byDefault()
                .customize(new CustomMapper<LoanDTO, Loan>() {
                    @Override
                    public void mapAtoB(LoanDTO loanDTO, Loan loan, MappingContext context) {
                        Client client = new Client();
                        client.setId(loanDTO.getClientId());
                        loan.setClient(client);
                    }

                    @Override
                    public void mapBtoA(Loan loan, LoanDTO loanDTO, MappingContext context) {
                        loanDTO.setClientId(loan.getClient().getId());
                        loanDTO.setLoanId(loan.getId());
                    }
                }).register();
        this.mapperFacade = factory.getMapperFacade();
    }

    public <Source, Target> Target map(Source source, Class<Target> target) {
        return mapperFacade.map(source, target);
    }

    public <Source, Target>List<Target> mapList(List<Source> source, Class<Target> target) {
        return mapperFacade.mapAsList(source, target);
    }
}
