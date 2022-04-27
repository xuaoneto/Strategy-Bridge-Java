package br.edu.ifpb.foodstore.service.payment;

import br.edu.ifpb.foodstore.service.log.impl.LogService;
import br.edu.ifpb.foodstore.service.payment.impl.PaymentServiceStrategyImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
public class PaymentServiceTest {

    @SpyBean
    private PaymentServiceStrategyImpl paymentService;

    @MockBean
    private LogService logService;

    @SneakyThrows
    @Test
    void doPaymentTest() {

        this.paymentService.doPayment(PaymentServiceStrategyImpl.PaymentType.CREDIT_CARD);
        InOrder orderVerifier = Mockito.inOrder(this.logService);
        orderVerifier.verify(this.logService).info("Do credit card payment!");

        this.paymentService.doPayment(PaymentServiceStrategyImpl.PaymentType.DEBIT);
        orderVerifier = Mockito.inOrder(this.logService);
        orderVerifier.verify(this.logService).info("Do debit payment!");

        this.paymentService.doPayment(PaymentServiceStrategyImpl.PaymentType.PAYPAL);
        orderVerifier = Mockito.inOrder(this.logService);
        orderVerifier.verify(this.logService).info("Do paypal payment!");

        this.paymentService.doPayment(PaymentServiceStrategyImpl.PaymentType.BILLET);
        orderVerifier = Mockito.inOrder(this.logService);
        orderVerifier.verify(this.logService).info("Do billet payment!");

    }

}
