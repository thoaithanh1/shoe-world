package root.app.controller;

import org.hibernate.validator.constraints.EAN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.app.entity.Category;
import root.app.entity.Color;
import root.app.service.impl.ColorServiceIpml;

import java.util.List;

@RestController
@RequestMapping("/api/v1/color")
public class ColorController {

    @Autowired
    private ColorServiceIpml colorService;

    @GetMapping
    public ResponseEntity<List<Color>> findAllColor() {
        List<Color> colors = colorService.findAll();
        return ResponseEntity.ok(colors);
    }

    @GetMapping("/page")
    public ResponseEntity<?> findColorByPage(@RequestParam(value = "pageNum", defaultValue = "0", required = false) Integer pageNum) {
        Page<Color> colors = colorService.findByPage(pageNum);
        return ResponseEntity.ok(colors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Color> findColorById(@PathVariable("id") Long colorId) {
        Color color = colorService.findById(colorId);
        return ResponseEntity.ok(color);
    }

    @PostMapping
    public ResponseEntity<?> saveColor(@RequestBody Color color) {
        Color saveColor = colorService.save(color);
        if (saveColor != null) {
            return new ResponseEntity<>(saveColor, HttpStatus.CREATED);
        } else {
            return ResponseEntity.ok("Có lỗi xảy ra khi thêm màu");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteColor(@PathVariable("id") Long colorId) {
        colorService.delete(colorId);
        return ResponseEntity.ok("Đã xóa thành công màu có mã " + colorId);
    }
}
