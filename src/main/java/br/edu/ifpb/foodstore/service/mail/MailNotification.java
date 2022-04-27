package br.edu.ifpb.foodstore.service.mail;

import br.edu.ifpb.foodstore.domain.Customer;

public interface MailNotification {
    //    padrao bridge que consiste em dividir classes em 2 fases abstração e implementação, aqui está a abstração...
    void sendMailNotificationToCustomer(String message, Customer customer);
    void sendMailNotificationToAdmin(String message);
}
