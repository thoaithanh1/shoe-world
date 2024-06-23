package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import root.app.entity.Color;
import root.app.exception.ObjectNotFoundException;
import root.app.repository.ColorRepository;
import root.app.service.extend.BaseService;

import java.util.List;

@Service
public class ColorServiceIpml implements BaseService<Color, Long> {

    private static final Integer COLOR_PER_PAGE = 5;

    @Autowired
    private ColorRepository colorRepository;

    @Override
    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    @Override
    public Page<Color> findByPage(Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum, COLOR_PER_PAGE);
        return colorRepository.findAll(pageable);
    }

    @Override
    public Color findById(Long id) {
        return colorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Không tìm thấy màu có mã " + id));
    }

    @Override
    public Color save(Color color) {
        return colorRepository.save(color);
    }

    @Override
    public void delete(Long id) {
        colorRepository.deleteById(id);
    }
}
