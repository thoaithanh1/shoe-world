package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import root.app.entity.Brand;
import root.app.service.impl.BrandServiceImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/brand")
public class BrandController {

    @Autowired
    private BrandServiceImpl brandService;

    @GetMapping
    public ResponseEntity<List<Brand>> findAllBrand() {
        List<Brand> brands = brandService.findAllBrand();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/page")
    public ResponseEntity<?> findBrandByPage(@RequestParam(value = "pageNum", defaultValue = "0", required = false) Integer pageNum) {
        Page<Brand> brand = brandService.findBrandByPage(pageNum);
        return ResponseEntity.ok(brand);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> findBrandById(@PathVariable("id") Long brandId) {
        Brand brand = brandService.findBrandById(brandId);
        return ResponseEntity.ok(brand);
    }

    @PostMapping
    public ResponseEntity<Brand> saveBrand(@RequestPart("brand") Brand brand,
                                           @RequestPart("brandImage") MultipartFile multipartFile) throws IOException {
        Brand saveBrand = brandService.saveBrand(brand, multipartFile);
        return ResponseEntity.ok(saveBrand);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBrand(@PathVariable("id") Long id) {
        Map<String, String> messages = new HashMap<>();
        brandService.deleteBrand(id);
        messages.put("message", "Xóa thành công nhãn hiệu có id là " + id);
        messages.put("status", "OK");
        return ResponseEntity.ok(messages);
    }
}
