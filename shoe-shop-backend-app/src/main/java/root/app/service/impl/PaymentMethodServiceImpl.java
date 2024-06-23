package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import root.app.entity.PaymentMethod;
import root.app.exception.ObjectNotFoundException;
import root.app.repository.PaymentMethodRepository;
import root.app.service.extend.BaseService;

import java.util.List;

@Service
public class PaymentMethodServiceImpl implements BaseService<PaymentMethod, Integer> {

    private static final Integer PAYMENT_METHOD_PER_PAGE = 5;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethod> findAll() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public Page<PaymentMethod> findByPage(Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAYMENT_METHOD_PER_PAGE);
        return paymentMethodRepository.findAll(pageable);
    }

    @Override
    public PaymentMethod findById(Integer id) {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Không tìm thấy phương thức thanh toán có id " + id));
    }

    @Override
    public PaymentMethod save(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public void delete(Integer id) {
        paymentMethodRepository.deleteById(id);
    }
}
