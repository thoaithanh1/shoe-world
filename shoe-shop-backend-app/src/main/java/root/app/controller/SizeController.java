package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.app.entity.Size;
import root.app.service.impl.SizeServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/size")
public class SizeController {

    @Autowired
    private SizeServiceImpl sizeService;

    @GetMapping
    public ResponseEntity<List<Size>> findAllSize() {
        List<Size> sizes = sizeService.findAll();
        return ResponseEntity.ok(sizes);
    }

    @GetMapping("/page")
    public ResponseEntity<?> findSizeByPage(@RequestParam(value = "pageNum", defaultValue = "0", required = false) Integer pageNum) {
        Page<Size> sizes = sizeService.findByPage(pageNum);
        return ResponseEntity.ok(sizes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Size> findSizeById(@PathVariable("id") Long sizeId) {
        Size size = sizeService.findById(sizeId);
        return ResponseEntity.ok(size);
    }

    @PostMapping
    public ResponseEntity<?> saveSize(@RequestBody Size size) {
        Size saveSize = sizeService.save(size);
        if (saveSize != null) {
            return new ResponseEntity<>(saveSize, HttpStatus.CREATED);
        } else {
            return ResponseEntity.ok("Có lỗi xảy ra khi thêm size");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSize(@PathVariable("id") Long sizeId) {
        sizeService.delete(sizeId);
        return ResponseEntity.ok("Đã xóa thành công size có mã " + sizeId);
    }
}
