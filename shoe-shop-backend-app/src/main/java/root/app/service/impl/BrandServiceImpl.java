package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import root.app.entity.Brand;
import root.app.exception.ObjectNotFoundException;
import root.app.repository.BrandRepository;
import root.app.service.BrandService;
import root.app.service.extend.BaseService;
import root.app.util.FileUploadUtil;

import java.io.IOException;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    private static final String UPLOAD_DIR_BRAND = "E:/Angular/shoe-shop-frontend-app/src/assets/image-data/brand-image/";

    private static final Integer BRAND_PER_PAGE = 5;

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> findAllBrand() {
        return brandRepository.findAll();
    }

    @Override
    public Page<Brand> findBrandByPage(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, BRAND_PER_PAGE);
        return brandRepository.findAll(pageable);
    }

    @Override
    public Brand findBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Không tồn tại nhãn hiệu có id là " + id));
    }

    @Override
    public Brand saveBrand(Brand brand, MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = multipartFile.getOriginalFilename();
            brand.setLogo(fileName);
            Brand saveBrand = brandRepository.save(brand);

            String uploadDir = UPLOAD_DIR_BRAND + saveBrand.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            return saveBrand;
        } else {
            if (brand.getLogo().isEmpty()) brand.setLogo(null);
            return brandRepository.save(brand);
        }
    }

    @Override
    public void deleteBrand(Long brandId) {
        brandRepository.deleteById(brandId);
    }
}
