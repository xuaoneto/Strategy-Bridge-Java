package br.edu.ifpb.foodstore.service.mail;

import br.edu.ifpb.foodstore.domain.Customer;
import br.edu.ifpb.foodstore.service.log.impl.LogService;
import br.edu.ifpb.foodstore.service.mail.impl.MailNotificationImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
public class MailNotificationTest {

    @SpyBean
    private MailNotificationImpl mailNotification;

    @MockBean
    private LogService logService;

    private Customer customer;

    @BeforeEach
    public void init() {
        customer = Customer.builder()
                .email("testuser@test.com")
                .name("Diego Pessoa")
                .address("Super address")
                .build();
    }

    @SneakyThrows
    @Test
    void sendMailNotificationToCustomerTest() {
        mailNotification.sendMailNotificationToCustomer("test message", customer);
        InOrder orderVerifier = Mockito.inOrder(logService);
        orderVerifier.verify(logService).info("send mail notification to "+customer.getEmail());
        orderVerifier.verify(logService).debug("test message");
    }

    @SneakyThrows
    @Test
    void sendMailNotificationToAdminTest() {
        String adminEmail = "contact@food-store.com";
        mailNotification.sendMailNotificationToAdmin("test message");
        InOrder orderVerifier = Mockito.inOrder(logService);
        orderVerifier.verify(logService).info("send mail notification to "+adminEmail);
        orderVerifier.verify(logService).debug("test message");
    }

}
