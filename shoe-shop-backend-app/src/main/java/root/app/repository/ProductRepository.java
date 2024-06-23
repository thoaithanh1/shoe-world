package root.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.app.dto.ProductCustom;
import root.app.dto.ProductShowClientDto;
import root.app.dto.ProductShowDto;
import root.app.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface    ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
                select new root.app.dto.ProductShowDto
                (
                min(pd.id), 
                concat('Giày ', p.model, ' ', p.name, ' Màu ', c.name), 
                pd.mainImage, 
                (select pdp.price from ProductDetail pdp where pdp.id = min(pd.id)), 
                (select pdp.discount from ProductDetail pdp where pdp.id = min(pd.id)), 
                (select pdp.createdDate from ProductDetail pdp where pdp.id = min(pd.id)),
                p.status
                ) 
                from Product p JOIN ProductDetail pd ON p.id = pd.product.id
                               JOIN Color c on pd.color.id = c.id
                group by concat('Giày ', p.model, ' ', p.name, ' Màu ', c.name), pd.mainImage, p.status
            """)
    List<ProductShowDto> findProductShow();

    @Query("""
                select new root.app.dto.ProductShowDto
                (
                min(pd.id), 
                concat('Giày ', p.model, ' ', p.name, ' Màu ', c.name), 
                pd.mainImage, 
                (select pdp.price from ProductDetail pdp where pdp.id = min(pd.id)), 
                (select pdp.discount from ProductDetail pdp where pdp.id = min(pd.id)), 
                (select pdp.createdDate from ProductDetail pdp where pdp.id = min(pd.id)),
                p.status
                ) 
                from Product p JOIN ProductDetail pd ON p.id = pd.product.id
                               JOIN Color c on pd.color.id = c.id
                where p.brand.id = :brandId or :brandId is null
                group by concat('Giày ', p.model, ' ', p.name, ' Màu ', c.name), pd.mainImage, p.status
            """)
    List<ProductShowDto> findProductShowByBrand(@Param("brandId") Long brandId);

    @Query("""
            select new root.app.dto.ProductShowClientDto
            (
            pd.id, 
            pd.mainImage, 
            concat('Giày ', p.model, ' ', p.name, ' Màu ', c.name), 
            pd.quantity, 
            pd.price, 
            pd.product.id,
            pd.color.id,
            pd.size.id,
            pd.size.name, 
            p.description, 
            pd.status) 
            from Product p JOIN ProductDetail pd ON p.id = pd.product.id join Color c on pd.color.id = c.id
            where pd.id = :productDetailId
            """)
    ProductShowClientDto findProductDetailById(@Param("productDetailId") Long productDetailId);

    @Query(value = """
                select 
                min(pd.id) as productId, 
                concat('Giày ', p.model, ' ', p.name, ' Màu ', c.name) as productName, 
                pd.main_image as productImage, 
                pd.price as price, 
                pd.discount as discount,
                min(pd.created_date) as createdDate,
                p.status as status
                from products p
                join product_detail pd on p.id = pd.product_id
                join sizes s on pd.size_id = s.id
                join brands b on p.brand_id = b.id
                join colors c on pd.color_id = c.id
                where (:keyword is null or p.name like %:keyword%)
                and (coalesce(:gender) is null or pd.gender = :gender)
                and (coalesce(:size) is null or s.id in (:size))
                and (coalesce(:brand) is null or b.id in (:brand))
                and (
                        (coalesce(:minPrice) is null or pd.price >= :minPrice) 
                        and 
                        (coalesce(:maxPrice) is null or pd.price <= :maxPrice)
                    )
                group by concat('Giày ', p.model, ' ', p.name, ' Màu ', c.name), pd.main_image, pd.price, pd.discount, p.status
            """, nativeQuery = true)
    Page<ProductCustom> filterProduct(@Param("keyword") String keyword,
                                      @Param("gender") Integer gender,
                                      @Param("brand") List<Long> brandList,
                                      @Param("size") List<Long> sizeList,
                                      @Param("minPrice") BigDecimal minPrice,
                                      @Param("maxPrice") BigDecimal maxPrice,
                                      Pageable pageable);
}
