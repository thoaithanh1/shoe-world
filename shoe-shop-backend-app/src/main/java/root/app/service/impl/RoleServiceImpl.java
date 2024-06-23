package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import root.app.entity.Role;
import root.app.exception.ObjectNotFoundException;
import root.app.repository.RoleRepository;
import root.app.service.extend.BaseService;

import java.util.List;

@Service
public class RoleServiceImpl implements BaseService<Role, Long> {

    private static final Integer ROLE_PER_PAGE = 5;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Page<Role> findByPage(Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum, ROLE_PER_PAGE);
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Không tìm thấy vai trò có mã " + id));
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
}
