package root.app.service;

import org.springframework.data.domain.Page;
import root.app.dto.ProductDto;
import root.app.dto.ProductFilterDto;
import root.app.dto.ProductShowDto;
import root.app.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    List<ProductDto> findAllProduct();

    Page<ProductDto> findProductByPage(int pageNum);

    Page<ProductShowDto> findPageProductDisplayHome(int pageNum, Long brandId);

    Page<ProductShowDto> filterProduct(ProductFilterDto productFilter);

    Product findById(Long productId);

    Product saveProduct(Product product);

    void deleteProduct(Long productId);
}
