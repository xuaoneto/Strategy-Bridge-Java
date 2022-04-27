package br.edu.ifpb.foodstore.service.order;

import br.edu.ifpb.foodstore.domain.Order;
import br.edu.ifpb.foodstore.service.order.impl.OrderException;
import br.edu.ifpb.foodstore.service.payment.impl.PaymentServiceStrategyImpl;

public interface OrderManager {
    //    padrao bridge que consiste em dividir classes em 2 fases abstração e implementação, aqui está a abstração...
    void payOrder(Order order, PaymentServiceStrategyImpl.PaymentType paymentType) ;
    void cancelOrder(Order order) throws OrderException;
}
