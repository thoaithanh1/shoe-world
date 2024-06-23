package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.app.dto.SizeShowClientDto;
import root.app.entity.Size;

import java.util.List;

public interface SizeRepository extends JpaRepository<Size, Long> {

    @Query("""
            select new root.app.dto.SizeShowClientDto
            (pd.size.id, pd.size.name, pd.quantity, pd.price) 
            from ProductDetail pd
            where pd.product.id = :productId and pd.color.id = :colorId
            order by pd.size.name asc
            """)
    List<SizeShowClientDto> findSizesByProductAndColor(@Param("productId") Long productId,
                                                       @Param("colorId") Long colorId);
}
