package root.app.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import root.app.dto.ProductShowClientDto;
import root.app.dto.ProductDetailDto;
import root.app.entity.ProductDetail;

import java.io.IOException;
import java.util.List;

public interface ProductDetailService {

    List<ProductDetailDto> getListProductDetail();

    Page<ProductDetailDto> getListProductDetailByPage(int pageNum);

    ProductShowClientDto findProductDetailAndImage(Long productDetailId);

    ProductDetail findProductDetailById(Long productDetailId);

    ProductDetail findProductDetailByProductAndSize(Long productId, Long sizeId);

    ProductDetail saveProductDetail(ProductDetail productDetail, MultipartFile multipartFile, MultipartFile[] subImages) throws IOException;

    void deleteProductDetail(Long productDetailId);
}
