package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.app.dto.OrderDetailDto;
import root.app.dto.OrderShowClientDto;
import root.app.entity.Order;
import root.app.service.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAllOrder(@RequestParam(value = "pageNum", defaultValue = "0", required = false) Integer pageNum,
                                         @RequestParam(value = "itemPerPage", defaultValue = "10", required = false) Integer itemPerPage) {
        Page<Order> orders = orderService.getAllOrder(pageNum, itemPerPage);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable("orderId") Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/order-detail/{orderId}")
    public ResponseEntity<?> getAllProductByOrder(@PathVariable("orderId") Long orderId) {
        List<OrderDetailDto> orders = orderService.getAllProductByOrder(orderId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllOrderByUser(@PathVariable("userId") Long userId) {
        List<OrderShowClientDto> orders = orderService.getAllOrderByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{userId}/{status}")
    public ResponseEntity<?> getAllOrderByUserAndStatus(@PathVariable("userId") Long userId,
                                                        @PathVariable("status") Integer status) {
        List<OrderShowClientDto> orders = orderService.getAllOrderByUserAndStatus(userId, status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getOrderByStatus(@PathVariable("status") Integer status,
                                              @RequestParam(value = "pageNum", defaultValue = "0", required = false) Integer pageNum,
                                              @RequestParam(value = "itemPerPage", defaultValue = "10", required = false) Integer itemPerPage,
                                              @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword) {
        Page<Order> order = orderService.getOrderByStatus(pageNum, itemPerPage, status, keyword);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<?> getOrderByDate(@PathVariable("date") Integer date,
                                            @RequestParam(value = "pageNum", defaultValue = "0", required = false) Integer pageNum,
                                            @RequestParam(value = "itemPerPage", defaultValue = "10", required = false) Integer itemPerPage,
                                            @RequestParam(value = "status", defaultValue = "0", required = false) Integer status) {
        Page<Order> order = orderService.getOrderByDate(pageNum, itemPerPage, date, status);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/update-status/{orderId}/{status}")
    public ResponseEntity<?> updateStatusOrder(@PathVariable("orderId") Long orderId,
                                               @PathVariable("status") Integer status) {
        Map<String, String> result = new HashMap<>();
        result.put("status", orderService.updateStatusOrder(orderId, status));
        return ResponseEntity.ok(result);
    }
}
