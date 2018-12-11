package io.fourfinanceit.services;

import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.LoanStatus;
import io.fourfinanceit.domain.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalTime;

import static io.fourfinanceit.services.TestUtils.setupLoan;
import static io.fourfinanceit.services.TestUtils.setupRequest;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanValidationServiceImplTest {

    @Mock
    private RiskAnalysisService riskAnalysisService;
    @Mock
    private IpAddressRequestService ipAddressRequestService;
    @Mock
    private ClockService clockService;

    @InjectMocks
    private LoanValidationServiceImpl validationService;

    private Loan loan;
    private HttpServletRequest request;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(validationService);
        when(clockService.getLocalDateNow()).thenReturn(LocalTime.of(1,0,0));
        when(ipAddressRequestService.getRequestCount(request)).thenReturn(1);
        loan = setupLoan();
        request = setupRequest();
    }

    @Test
    public void testNotMaxAmount() {
        loan.setAmount(BigDecimal.ONE);
        validationService.validateRisk(loan, request);
        verify(riskAnalysisService, never()).makeAnalysis(loan);
    }

    @Test
    public void testNegativeAmount() {
        loan.setAmount(BigDecimal.ONE.negate());
        validationService.validateRisk(loan, request);
        verify(riskAnalysisService, never()).makeAnalysis(loan);
    }

    @Test
    public void testMaxAmount() {
        validationService.validateRisk(loan, request);
        verify(riskAnalysisService).makeAnalysis(loan);
    }

    @Test
    public void testOverMaxAmount() {
        loan.setAmount(loan.getAmount().add(BigDecimal.ONE));
        validationService.validateRisk(loan, request);
        verify(riskAnalysisService).makeAnalysis(loan);
    }

    @Test
    public void testNotRiskyTime() {
        when(clockService.getLocalDateNow()).thenReturn(LocalTime.of(10,0,0));
        validationService.validateRisk(loan, request);
        verify(riskAnalysisService, never()).makeAnalysis(loan);
    }

    @Test
    public void testMidnightTime() {
        when(clockService.getLocalDateNow()).thenReturn(LocalTime.of(0,0,0));
        validationService.validateRisk(loan, request);
        verify(riskAnalysisService, never()).makeAnalysis(loan);
    }

    @Test
    public void testMaxApplicationPerIp() {
        when(ipAddressRequestService.getRequestCount(request)).thenReturn(3);
        when(clockService.getLocalDateNow()).thenReturn(LocalTime.of(15,30,45));
        validationService.validateRisk(loan, request);
        verify(riskAnalysisService).makeAnalysis(loan);
    }

    @Test
    public void testOverMaxApplicationPerIp() {
        when(ipAddressRequestService.getRequestCount(request)).thenReturn(4);
        when(clockService.getLocalDateNow()).thenReturn(LocalTime.of(15,30,45));
        validationService.validateRisk(loan, request);
        verify(riskAnalysisService).makeAnalysis(loan);
    }

    @Test
    public void testNotMaxApplicationPerIp() {
        when(ipAddressRequestService.getRequestCount(request)).thenReturn(2);
        when(clockService.getLocalDateNow()).thenReturn(LocalTime.of(15,30,45));
        validationService.validateRisk(loan, request);
        verify(riskAnalysisService, never()).makeAnalysis(loan);
    }

    @Test
    public void validateExtensionSuccessful() {
        validationService.validateExtension(loan);
    }

    @Test(expected = ValidationException.class)
    public void validateStatusExtended() {
        loan.setStatus(LoanStatus.EXTENDED);
        validationService.validateExtension(loan);
    }

    @Test(expected = ValidationException.class)
    public void validateStatusClosed() {
        loan.setStatus(LoanStatus.CLOSED);
        validationService.validateExtension(loan);
    }

}