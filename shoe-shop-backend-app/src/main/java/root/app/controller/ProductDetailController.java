package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import root.app.dto.ProductShowClientDto;
import root.app.dto.ProductDetailDto;
import root.app.entity.ProductDetail;
import root.app.service.ProductDetailService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/product-detail")
public class ProductDetailController {

    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping
    public ResponseEntity<?> findAllProductDetail() {
        List<ProductDetailDto> productDetails = productDetailService.getListProductDetail();
        return ResponseEntity.ok(Objects.requireNonNullElse(productDetails, "Không có sản phẩm chi tiết nào!"));
    }

    @GetMapping("/page")
    public ResponseEntity<?> findProductDetailByPage(@RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum) {
        Page<ProductDetailDto> productDetails = productDetailService.getListProductDetailByPage(pageNum);
        return ResponseEntity.ok(Objects.requireNonNullElse(productDetails, "Không có sản phẩm chi tiết nào ở trang này!"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProductDetailById(@PathVariable("id") Long productDetailId) {
        ProductDetail productDetail = productDetailService.findProductDetailById(productDetailId);
        return ResponseEntity.ok(Objects.requireNonNullElse(productDetail,
                "Không tìm thấy sản phẩm chi tiết nào có mã " + productDetailId));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findProductDetailAndImage(@PathVariable("id") Long productDetailId) {
        ProductShowClientDto productDetail = productDetailService.findProductDetailAndImage(productDetailId);
        return ResponseEntity.ok(productDetail);
    }

    @GetMapping("/detail-cart/{productId}/{sizeId}")
    public ResponseEntity<?> findProductDetailByProductAndSize(@PathVariable("productId") Long productId,
                                                               @PathVariable("sizeId") Long sizeId) {
        ProductDetail productDetail = productDetailService.findProductDetailByProductAndSize(productId, sizeId);
        return ResponseEntity.ok(productDetail);
    }

    @PostMapping
    public ResponseEntity<?> saveProductDetail(@RequestPart("productDetail") ProductDetail productDetail,
                                               @RequestPart("productImage")MultipartFile multipartFile,
                                               @RequestPart("productSubImage")MultipartFile[] subImages) throws IOException {
        ProductDetail saveProductDetail = productDetailService.saveProductDetail(productDetail, multipartFile, subImages);
        if (saveProductDetail == null) {
            return new ResponseEntity<>("Thêm sản phẩm thất bại", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(saveProductDetail, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductDetail(@PathVariable("id") Long productDetailId) {
        productDetailService.deleteProductDetail(productDetailId);
        return ResponseEntity.ok("Xóa thành công chi tiết sản phẩm có mã " + productDetailId);
    }
}
