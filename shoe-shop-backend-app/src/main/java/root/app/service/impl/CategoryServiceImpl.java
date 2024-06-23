package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import root.app.entity.Category;
import root.app.exception.ObjectNotFoundException;
import root.app.repository.CategoryRepository;
import root.app.service.extend.BaseService;

import java.util.List;

@Service
public class CategoryServiceImpl implements BaseService<Category, Long> {

    private static final Integer CATEGORY_PER_PAGE = 5;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> findByPage(Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum, CATEGORY_PER_PAGE);
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Không tồn tại thể loại có id là " + id));
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
