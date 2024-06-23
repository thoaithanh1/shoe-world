package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.app.dto.ImagesShowClientDto;
import root.app.entity.ProductImages;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImages, Long> {

    @Query("select new root.app.dto.ImagesShowClientDto(pd.id, pd.name) from ProductImages pd where pd.productDetail.id = :productDetailId")
    List<ImagesShowClientDto> findImagesByProductDetail(@Param("productDetailId") Long productDetailId);
}
