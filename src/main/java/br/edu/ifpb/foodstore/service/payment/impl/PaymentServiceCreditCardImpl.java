package br.edu.ifpb.foodstore.service.payment.impl;

import br.edu.ifpb.foodstore.service.log.impl.LogService;
import br.edu.ifpb.foodstore.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentServiceCreditCardImpl implements PaymentService {

    private LogService logService;

    @Override
    public void doPayment() {
       logService.info("Do credit card payment!");
    }
}
