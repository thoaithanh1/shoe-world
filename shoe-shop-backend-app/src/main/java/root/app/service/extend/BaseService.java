package root.app.service.extend;

import org.springframework.data.domain.Page;

import java.util.List;

public interface BaseService<T, D> {

    List<T> findAll();

    Page<T> findByPage(Integer pageNum);

    T findById(D id);

    T save(T t);

    void delete(D id);
}
