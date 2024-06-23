package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.app.entity.Brand;
import root.app.entity.Category;
import root.app.service.impl.CategoryServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> findAllCategory() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/page")
    public ResponseEntity<?> findBrandByPage(@RequestParam(value = "pageNum", defaultValue = "0", required = false) Integer pageNum) {
        Page<Category> categories = categoryService.findByPage(pageNum);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findCategoryById(@PathVariable("id") Long categoryId) {
        Category category = categoryService.findById(categoryId);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<Category> saveCategory(@RequestBody Category category) {
        Category saveCategory = categoryService.save(category);
        return ResponseEntity.ok(saveCategory);
    }

    @PutMapping
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
        Category saveCategory = categoryService.save(category);
        return ResponseEntity.ok(saveCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable("id") Long id) {
        Map<String, String> messages = new HashMap<>();
        categoryService.delete(id);
        messages.put("message", "Xóa thành công thể loại có id là " + id);
        messages.put("status", "OK");
        return ResponseEntity.ok(messages);
    }
}
