package br.edu.ifpb.foodstore.service.order;

import br.edu.ifpb.foodstore.domain.Customer;
import br.edu.ifpb.foodstore.domain.Order;
import br.edu.ifpb.foodstore.domain.OrderItem;
import br.edu.ifpb.foodstore.domain.Product;
import br.edu.ifpb.foodstore.service.log.impl.LogService;
import br.edu.ifpb.foodstore.service.mail.impl.MailNotificationImpl;
import br.edu.ifpb.foodstore.service.order.impl.OrderException;
import br.edu.ifpb.foodstore.service.order.impl.OrderManagerImpl;
import br.edu.ifpb.foodstore.service.payment.impl.PaymentServiceStrategyImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
public class OrderManagerTest {

    @SpyBean
    private OrderManagerImpl orderManager;

    @MockBean
    private PaymentServiceStrategyImpl paymentService;

    @MockBean
    private MailNotificationImpl mailNotification;

    @MockBean
    private LogService logService;

    private Order order;

    @BeforeEach
    public void init() {
        order = Order.builder()
                .id(1L)
                .status(Order.OrderStatus.IN_PROGRESS)
                .customer(Customer.builder()
                        .email("testuser@test.com")
                        .name("Diego Pessoa")
                        .address("Super address")
                        .build())
                .creationDate(Calendar.getInstance())
                .items(Collections.singletonList(OrderItem.builder()
                        .product(Product.builder()
                                .name("Pastel de Vento")
                                .price(BigDecimal.valueOf(5.50))
                                .description("Pastel muito bom")
                                .build())
                        .build()))
                .build();
    }

    @SneakyThrows
    @Test
    void payOrderTest_success() {
        PaymentServiceStrategyImpl.PaymentType paymentType = PaymentServiceStrategyImpl.PaymentType.CREDIT_CARD;
        orderManager.payOrder(order, paymentType);
        InOrder orderVerifier = Mockito.inOrder(paymentService, mailNotification, logService);
        orderVerifier.verify(paymentService).doPayment(paymentType);
        orderVerifier.verify(mailNotification).sendMailNotificationToAdmin("Order 1 completed successfully");
        orderVerifier.verify(mailNotification).sendMailNotificationToCustomer("Order 1 completed successfully", order.getCustomer());
        orderVerifier.verify(logService).info("payment finished");
    }

    @SneakyThrows
    @Test
    void payOrderTest_error() {
        PaymentServiceStrategyImpl.PaymentType paymentType = PaymentServiceStrategyImpl.PaymentType.BILLET;
        doThrow(new Exception(("unknown payment method"))).when(paymentService).doPayment(eq(paymentType));
        orderManager.payOrder(order, paymentType);
        InOrder orderVerifier = Mockito.inOrder(paymentService, mailNotification, logService);
        orderVerifier.verify(paymentService).doPayment(paymentType);
        orderVerifier.verify(logService).error("payment refused");
    }

    @SneakyThrows
    @Test
    void cancelOrderTest_inProgress() {
        orderManager.cancelOrder(order);
        assertThat(order.getStatus(), equalTo(Order.OrderStatus.CANCELED));
        InOrder orderVerifier = Mockito.inOrder(logService, mailNotification);
        orderVerifier.verify(logService).info("Canceling in progress order");
        orderVerifier.verify(mailNotification).sendMailNotificationToAdmin("Order 1 canceled");
        orderVerifier.verify(mailNotification).sendMailNotificationToCustomer("Order 1 canceled", order.getCustomer());
        orderVerifier.verify(logService).debug("order 1 canceled");
    }

    @SneakyThrows
    @Test
    void cancelOrderTest_canceled() {
        order.setStatus(Order.OrderStatus.CANCELED);
        assertThrows(OrderException.class, () -> orderManager.cancelOrder(order));
    }

    @SneakyThrows
    @Test
    void cancelOrderTest_paymentRefused() {
        order.setStatus(Order.OrderStatus.PAYMENT_REFUSED);
        orderManager.cancelOrder(order);
        assertThat(order.getStatus(), equalTo(Order.OrderStatus.CANCELED));
        InOrder orderVerifier = Mockito.inOrder(logService, mailNotification);
        orderVerifier.verify(logService).info("Canceling refused order");
        orderVerifier.verify(mailNotification).sendMailNotificationToAdmin("Order 1 canceled");
        orderVerifier.verify(mailNotification).sendMailNotificationToCustomer("Order 1 canceled", order.getCustomer());
        orderVerifier.verify(logService).debug("order 1 canceled");
    }

    @SneakyThrows
    @Test
    void cancelOrderTest_paymentSuccess() {
        order.setStatus(Order.OrderStatus.PAYMENT_SUCCESS);
        orderManager.cancelOrder(order);
        assertThat(order.getStatus(), equalTo(Order.OrderStatus.CANCELED));
        InOrder orderVerifier = Mockito.inOrder(logService, mailNotification);
        orderVerifier.verify(logService).info("Canceling already paid order");
        orderVerifier.verify(mailNotification).sendMailNotificationToAdmin("Order 1 canceled");
        orderVerifier.verify(mailNotification).sendMailNotificationToCustomer("Order 1 canceled", order.getCustomer());
        orderVerifier.verify(logService).debug("order 1 canceled");
    }
}
