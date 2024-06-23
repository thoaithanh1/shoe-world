package root.app.service;

import org.springframework.data.domain.Page;
import root.app.dto.OrderDetailDto;
import root.app.dto.OrderShowClientDto;
import root.app.entity.Order;

import java.util.List;

public interface OrderService {

    Page<Order> getAllOrder(int pageNum, int itemPerPage);

    Order getOrderById(Long orderId);

    List<OrderDetailDto> getAllProductByOrder(Long orderId);

    List<OrderShowClientDto> getAllOrderByUser(Long userId);

    List<OrderShowClientDto> getAllOrderByUserAndStatus(Long userId, Integer status);

    Page<Order> getOrderByStatus(int pageNum, int itemPerPage, Integer status, String keyword);

    Page<Order> getOrderByDate(int pageNum, int itemPerPage, Integer date, Integer status);

    String updateStatusOrder(Long orderId, Integer status);
}
