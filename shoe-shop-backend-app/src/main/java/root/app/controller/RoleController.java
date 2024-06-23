package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.app.entity.Role;
import root.app.service.impl.RoleServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping
    public ResponseEntity<?> findAllRole() {
        List<Role> roles = roleService.findAll();
        if(roles == null) {
            return new ResponseEntity<>("Không có vai trò nào!", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findRoleById(@PathVariable("id") Long roleId) {
        Role role = roleService.findById(roleId);
        return ResponseEntity.ok(role);
    }

    @PostMapping
    public ResponseEntity<?> saveRole(@RequestBody Role role) {
        Role saveRole = roleService.save(role);
        if (saveRole != null) {
            return new ResponseEntity<>(saveRole, HttpStatus.CREATED);
        } else {
            return ResponseEntity.ok("Có lỗi xảy ra khi thêm vai trò");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable("id") Long roleId) {
        roleService.delete(roleId);
        return ResponseEntity.ok("Đã xóa thành công vai trò có mã " + roleId);
    }
}
