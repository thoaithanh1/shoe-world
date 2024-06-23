package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.app.dto.OrderDetailDto;
import root.app.entity.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("select new root.app.dto.OrderDetailDto(o.id, o.name, o.productDetail.id, o.productDetail.mainImage, o.quantity, o.price, o.size, o.color) " +
           "from OrderDetail o where o.order.id = :orderId")
    List<OrderDetailDto> getAllOrderDetailByOrder(@Param("orderId") Long orderId);
}
