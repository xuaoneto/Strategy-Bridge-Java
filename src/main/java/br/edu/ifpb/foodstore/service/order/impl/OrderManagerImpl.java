package br.edu.ifpb.foodstore.service.order.impl;

import br.edu.ifpb.foodstore.domain.Order;
import br.edu.ifpb.foodstore.service.log.impl.LogService;
import br.edu.ifpb.foodstore.service.mail.impl.MailNotificationImpl;
import br.edu.ifpb.foodstore.service.order.OrderManager;
import br.edu.ifpb.foodstore.service.payment.impl.PaymentServiceStrategyImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderManagerImpl implements OrderManager {

    private final PaymentServiceStrategyImpl paymentService;
    private final MailNotificationImpl mailNotification;
    private final LogService logService;

    @Override
    public void payOrder(Order order, PaymentServiceStrategyImpl.PaymentType paymentType) {
        order.setStatus(Order.OrderStatus.IN_PROGRESS);
        try {
            paymentService.doPayment(paymentType);
            order.setStatus(Order.OrderStatus.PAYMENT_SUCCESS);
            mailNotification.sendMailNotificationToAdmin(String.format("Order %d completed successfully", order.getId()));
            mailNotification.sendMailNotificationToCustomer(String.format("Order %d completed successfully", order.getId()), order.getCustomer());
            logService.info("payment finished");
        } catch (Exception e) {
            logService.error("payment refused");
            order.setStatus(Order.OrderStatus.PAYMENT_REFUSED);
            mailNotification.sendMailNotificationToAdmin(String.format("Order %d refused", order.getId()));
        }
    }
    @Override
    public void cancelOrder(Order order) throws OrderException {
        switch(order.getStatus()) {
            case CANCELED:
                throw new OrderException("Order already canceled!");
            case IN_PROGRESS:
                logService.info("Canceling in progress order");
                break;
            case PAYMENT_REFUSED:
                logService.info("Canceling refused order");
                break;
            case PAYMENT_SUCCESS:
                logService.info("Canceling already paid order");
                break;
        }
        order.setStatus(Order.OrderStatus.CANCELED);
        mailNotification.sendMailNotificationToAdmin(String.format("Order %d canceled", order.getId()));
        mailNotification.sendMailNotificationToCustomer(String.format("Order %d canceled", order.getId()), order.getCustomer());
        logService.debug(String.format("order %d canceled", order.getId()));
    }

}
