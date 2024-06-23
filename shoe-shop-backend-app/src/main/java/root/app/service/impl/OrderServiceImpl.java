package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import root.app.dto.OrderDetailDto;
import root.app.dto.OrderShowClientDto;
import root.app.dto.ProductShowDto;
import root.app.entity.Order;
import root.app.exception.ObjectNotFoundException;
import root.app.repository.OrderDetailRepository;
import root.app.repository.OrderRepository;
import root.app.service.OrderService;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public Page<Order> getAllOrder(int pageNum, int itemPerPage) {
        Pageable pageable = PageRequest.of(pageNum, itemPerPage);
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Không tìm thấy order có mã " + orderId));
    }

    @Override
    public List<OrderDetailDto> getAllProductByOrder(Long orderId) {
        return orderDetailRepository.getAllOrderDetailByOrder(orderId);
    }

    @Override
    public List<OrderShowClientDto> getAllOrderByUser(Long userId) {
        List<Order> orders = orderRepository.getAllOrderByUser(userId);
        return fillDataInOrderDto(orders);
    }

    @Override
    public List<OrderShowClientDto> getAllOrderByUserAndStatus(Long userId, Integer status) {
        List<Order> orders = orderRepository.getAllOrderByUserAndStatus(userId, status);
        return fillDataInOrderDto(orders);
    }

    @Override
    public Page<Order> getOrderByStatus(int pageNum, int itemPerPage, Integer status, String keyword) {
        Pageable pageable = PageRequest.of(pageNum, itemPerPage).withSort(Sort.by("createdDate").descending());
        return orderRepository.getOrderByStatus(status, keyword, pageable);
    }

    @Override
    public Page<Order> getOrderByDate(int pageNum, int itemPerPage, Integer date, Integer status) {
        Pageable pageable = PageRequest.of(pageNum, itemPerPage);
        List<Order> order;
        if(date == 1) {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = dateFormat.format(now);
            order = orderRepository.getOrderByDate(format, status);
        }else if(date == 2) {
            ZonedDateTime now = ZonedDateTime.now();
            LocalDate currentDate = now.toLocalDate();
            WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
            int currentWeek = currentDate.get(weekFields.weekOfWeekBasedYear());
            order = orderRepository.getOrderByWeek(currentWeek, status);
        }else if(date == 3) {
            LocalDate currentDate = LocalDate.now();
            int month = currentDate.getMonthValue();
            order = orderRepository.getOrderByMonth(month, status);
        } else {
            return orderRepository.findAll(pageable);
        }

        int start = pageNum * itemPerPage;
        int end = Math.min((start + itemPerPage), order.size());
        List<Order> pageProduct = order.subList(start, end);
        return new PageImpl<>(pageProduct, PageRequest.of(pageNum, itemPerPage), order.size());
    }

    @Override
    public String updateStatusOrder(Long orderId, Integer status) {
        return orderRepository.updateStatusOrder(orderId, status) == 1 ? "OK":"FAILED";
    }

    private List<OrderShowClientDto> fillDataInOrderDto(List<Order> orders) {
        List<OrderShowClientDto> orderClients = new ArrayList<>();
        for (Order order : orders) {
            List<OrderDetailDto> orderDetails = orderDetailRepository.getAllOrderDetailByOrder(order.getId());
            OrderShowClientDto orderClient =
                    new OrderShowClientDto(
                            order.getId(), order.getCode(),
                            order.getTotal(), order.getShippingFee(),
                            order.getStatus(), order.getCreatedDate(),
                            order.getCancelledDate(), order.getCompletedDate(),
                            orderDetails
                    );
            orderClients.add(orderClient);
        }
        return orderClients;
    }


}
