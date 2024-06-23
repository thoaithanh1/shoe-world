package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import root.app.entity.Brand;
import root.app.entity.PaymentMethod;
import root.app.service.impl.PaymentMethodServiceImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment-method")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodServiceImpl paymentMethodService;

    @GetMapping
    public ResponseEntity<List<PaymentMethod>> findAllPaymentMethod() {
        List<PaymentMethod> paymentMethods = paymentMethodService.findAll();
        return ResponseEntity.ok(paymentMethods);
    }

    @GetMapping("/page")
    public ResponseEntity<?> findPaymentMethodByPage(@RequestParam(value = "pageNum", defaultValue = "0", required = false) Integer pageNum) {
        Page<PaymentMethod> paymentMethods = paymentMethodService.findByPage(pageNum);
        return ResponseEntity.ok(paymentMethods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> findPaymentMethodById(@PathVariable("id") Integer id) {
        PaymentMethod paymentMethod = paymentMethodService.findById(id);
        return ResponseEntity.ok(paymentMethod);
    }

    @PostMapping
    public ResponseEntity<PaymentMethod> savePaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        PaymentMethod savePaymentMethod = paymentMethodService.save(paymentMethod);
        return ResponseEntity.ok(savePaymentMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePaymentMethod(@PathVariable("id") Integer id) {
        Map<String, String> messages = new HashMap<>();
        paymentMethodService.delete(id);
        messages.put("message", "Xóa thành công nhãn hiệu có id là " + id);
        messages.put("status", "OK");
        return ResponseEntity.ok(messages);
    }
}
