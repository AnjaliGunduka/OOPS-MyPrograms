package com.junodx.api.services.commerce;

import com.junodx.api.models.payment.PaymentProcessorProvider;
import com.junodx.api.repositories.commerce.PaymentProcessorProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PaymentProcessorProviderService {
    @Autowired
    private PaymentProcessorProviderRepository paymentProcessorProviderRepository;

    public Optional<PaymentProcessorProvider> getPaymentProcessorProviderByName(String name){
        return paymentProcessorProviderRepository.findPaymentProcessorProvider(name);
    }

    public Optional<PaymentProcessorProvider> getOne(String id){
        return paymentProcessorProviderRepository.findById(id);
    }

    public PaymentProcessorProvider save(PaymentProcessorProvider provider){
        return paymentProcessorProviderRepository.save(provider);
    }
}
