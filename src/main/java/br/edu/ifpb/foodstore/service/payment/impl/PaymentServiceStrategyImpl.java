package br.edu.ifpb.foodstore.service.payment.impl;


import br.edu.ifpb.foodstore.service.payment.PaymentService;

public class PaymentServiceStrategyImpl {

    public enum PaymentType {
        CREDIT_CARD, DEBIT, BILLET, PAYPAL
    }

    public void doPayment(PaymentType paymentType ) throws Exception{

        switch (paymentType)  {
            case CREDIT_CARD:
                new PaymentServiceCreditCardImpl().doPayment();
            case DEBIT:
                new PaymentServiceDebitImpl().doPayment();
            case PAYPAL:
                new PaymentServicePaypalImpl().doPayment();
            case BILLET:
                new PaymentServiceBilletImpl().doPayment();
            default:
                throw new Exception("unknown payment method");
        }
    }

}
