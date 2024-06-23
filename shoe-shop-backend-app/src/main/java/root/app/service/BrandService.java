package root.app.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import root.app.entity.Brand;

import java.io.IOException;
import java.util.List;

public interface BrandService {

    List<Brand> findAllBrand();

    Page<Brand> findBrandByPage(int pageNum);

    Brand findBrandById(Long id);

    Brand saveBrand(Brand brand, MultipartFile multipartFile) throws IOException;

    void deleteBrand(Long brandId);
}
