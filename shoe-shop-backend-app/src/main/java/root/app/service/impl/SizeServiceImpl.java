package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import root.app.entity.Size;
import root.app.exception.ObjectNotFoundException;
import root.app.repository.SizeRepository;
import root.app.service.extend.BaseService;

import java.util.List;

@Service
public class SizeServiceImpl implements BaseService<Size, Long> {

    private static final Integer SIZE_PER_PAGE = 5;

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<Size> findAll() {
        return sizeRepository.findAll();
    }

    @Override
    public Page<Size> findByPage(Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum, SIZE_PER_PAGE);
        return sizeRepository.findAll(pageable);
    }

    @Override
    public Size findById(Long id) {
        return sizeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Không tìm thấy size có mã " + id));
    }

    @Override
    public Size save(Size size) {
        return sizeRepository.save(size);
    }

    @Override
    public void delete(Long id) {
        sizeRepository.deleteById(id);
    }
}
