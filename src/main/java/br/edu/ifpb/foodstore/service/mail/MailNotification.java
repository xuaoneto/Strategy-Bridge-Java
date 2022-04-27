package br.edu.ifpb.foodstore.service.mail;

import br.edu.ifpb.foodstore.domain.Customer;

public interface MailNotification {

    void sendMailNotificationToCustomer(String message, Customer customer);
    void sendMailNotificationToAdmin(String message);
}
