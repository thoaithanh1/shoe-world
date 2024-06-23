package root.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import root.app.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.user.id = :userId")
    List<Order> getAllOrderByUser(@Param("userId") Long userId);

    @Query("select o from Order o where o.user.id = :userId and o.status = :status")
    List<Order> getAllOrderByUserAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

    @Query("""
            select o from Order o where (o.status = :status or :status = 0)
            and (
                    (o.code is null or o.code like %:keyword%) 
                    or 
                    (o.user.phoneNumber is null or o.user.phoneNumber like %:keyword%)
                )
            """)
    Page<Order> getOrderByStatus(@Param("status") Integer status,
                                 @Param("keyword") String keyword,
                                 Pageable pageable);

    @Query(value = """
            select * from orders o
            where o.created_date like %?1% and (o.status = ?2 or ?2 = 0)
            order by o.created_date desc
            """, nativeQuery = true)
    List<Order> getOrderByDate(String date, Integer status);

    @Query(value = """
            select * from orders o
            where week(o.created_date) = ?1 and (o.status = ?2 or ?2 = 0)
            order by o.created_date desc
            """, nativeQuery = true)
    List<Order> getOrderByWeek(Integer date, Integer status);

    @Query(value = """
            select * from orders o
            where month(o.created_date) = ?1 and (o.status = ?2 or ?2 = 0)
            order by o.created_date desc
            """, nativeQuery = true)
    List<Order> getOrderByMonth(Integer date, Integer status);

    @Transactional
    @Modifying
    @Query("update Order o set o.status = :status where o.id = :id")
    int updateStatusOrder(@Param("id") Long id, @Param("status") Integer status);
}
