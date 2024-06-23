package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.app.dto.ProductDto;
import root.app.dto.ProductFilterDto;
import root.app.dto.ProductShowDto;
import root.app.entity.Product;
import root.app.service.ProductService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> findAllProduct() {
        List<ProductDto> products = productService.findAllProduct();
        if (products.isEmpty()) {
            Map<String, String> messages = new HashMap<>();
            messages.put("message", "Không có sản phẩm nào!");
            return ResponseEntity.ok(messages);
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/page")
    public ResponseEntity<?> findProductByPage(
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum) {
        Page<ProductDto> products = productService.findProductByPage(pageNum);
        if (products.isEmpty()) {
            Map<String, String> messages = new HashMap<>();
            messages.put("message", "Không có sản phẩm nào ở trang này!");
            return ResponseEntity.ok(messages);
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/show/page")
    public ResponseEntity<?> findPageProductDisplayHome(
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "brandId", required = false, defaultValue = "0") Long brandId) {
        Page<ProductShowDto> products = productService.findPageProductDisplayHome(pageNum, brandId);
        if (products.isEmpty()) {
            Map<String, String> messages = new HashMap<>();
            messages.put("message", "Không có sản phẩm nào ở trang này!");
            return ResponseEntity.ok(messages);
        }
        return ResponseEntity.ok(products);
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterProduct(@RequestBody ProductFilterDto productFilter) {
        Page<ProductShowDto> products =
                productService.filterProduct(productFilter);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProductById(@PathVariable("id") Long productId) {
        Product product = productService.findById(productId);
        if(product == null) {
            return new ResponseEntity<>("Không tìm thấy sản phẩm có mã " + productId, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        Product saveProduct = productService.saveProduct(product);
        if (saveProduct == null) {
            Map<String, String> messages = new HashMap<>();
            messages.put("message", "Thêm sản phẩm thất bại");
            return ResponseEntity.ok(messages);
        }
        return ResponseEntity.ok(saveProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Xóa thành công sản phẩm có mã là " + productId);
    }
}
