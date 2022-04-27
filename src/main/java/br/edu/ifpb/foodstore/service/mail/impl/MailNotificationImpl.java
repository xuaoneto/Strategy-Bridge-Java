package br.edu.ifpb.foodstore.service.mail.impl;

import br.edu.ifpb.foodstore.domain.Customer;
import br.edu.ifpb.foodstore.service.log.impl.LogService;
import br.edu.ifpb.foodstore.service.mail.MailNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailNotificationImpl implements MailNotification {

    private final LogService logService;

    private String emailAdministration = "contact@food-store.com";

    @Override
    public void sendMailNotificationToCustomer(String message, Customer customer) {
        logService.info("send mail notification to "+ customer.getEmail());
        logService.debug(message);
    }
    @Override
    public void sendMailNotificationToAdmin(String message) {
        logService.info("send mail notification to "+emailAdministration);
        logService.debug(message);
    }


}
