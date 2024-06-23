package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.app.entity.Promotion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Query("""
            select p from Promotion p where p.code like %:keyword% or p.name like %:keyword%
            """)
    List<Promotion> findPromotionsByCodeOrName(@Param("keyword") String keyword);

    List<Promotion> findPromotionsByStatus(Boolean status);

    @Query("""
            select p from Promotion p where p.endDate < :today
            """)
    List<Promotion> findPromotionByExpire(@Param("today") Date today);

    @Query("""
            select p from Promotion p 
            where (:operator = '>' and p.endDate > :today) or (:operator = '<' and p.endDate < :today)
            """)
    List<Promotion> findPromotionByActivated(@Param("today") Date today, @Param("operator") String operator);

    @Query("""
            select p from Promotion p 
            where ((p.code is null or p.code like %:keyword%) or (p.name is null or p.name like %:keyword%))
            and (:skip = 'skip' or p.status = :status)
            and (:allMethod = '' or p.discountMethod = :method)
            and ((p.endDate >= :today and :operator = '>') or (p.endDate < :today and :operator = '<') or (:operator = ''))
            """)
    List<Promotion> filterPromotion(@Param("keyword") String keyword,
                                    @Param("status") Boolean status,
                                    @Param("skip") String skip,
                                    @Param("method") Integer method,
                                    @Param("allMethod") String all,
                                    @Param("today") Date today,
                                    @Param("operator") String operator);

    @Query(value = """
            select * from promotions p
            where p.start_date <= ?1 and p.end_date >= ?1
            and p.status = true
            and discount_method = 1
            and p.conditions_apply <= ?2
            order by p.created_date
            limit 1
            """, nativeQuery = true)
    Promotion findPromotionByDateAndCondition(Date today, BigDecimal condition);
}
