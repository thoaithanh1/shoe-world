package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import root.app.dto.ProductCustom;
import root.app.dto.ProductDto;
import root.app.dto.ProductFilterDto;
import root.app.dto.ProductShowDto;
import root.app.entity.Product;
import root.app.exception.ObjectNotFoundException;
import root.app.mapper.CustomMapper;
import root.app.repository.ProductRepository;
import root.app.service.ProductService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String UPLOAD_DIR_PRODUCT = "E:/Angular/shoe-shop-frontend-app/src/assets/image-data/product-image/";

    private static final Integer PRODUCT_PER_PAGE = 5;

    private static final Integer PRODUCT_DISPLAY_HOME_PER_PAGE = 9;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomMapper customMapper;

    @Override
    public List<ProductDto> findAllProduct() {
        return productRepository.findAll(Sort.by("name").ascending())
                .stream()
                .map(p -> customMapper.convertEntityToDto(p))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDto> findProductByPage(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PRODUCT_PER_PAGE);
        Page<Product> all = productRepository.findAll(pageable);
        List<ProductDto> productDtos = productRepository.findAll(pageable)
                .stream()
                .map(p -> customMapper.convertEntityToDto(p))
                .collect(Collectors.toList());
        return new PageImpl<>(productDtos, pageable, all.getTotalElements());
    }

    @Override
    public Page<ProductShowDto> findPageProductDisplayHome(int pageNum, Long brandId) {
        List<ProductShowDto> products;
        if(brandId == 0) {
            products = productRepository.findProductShow();
        } else {
            products = productRepository.findProductShowByBrand(brandId);
        }
        int pageSize = PRODUCT_DISPLAY_HOME_PER_PAGE;
        int start = pageNum * pageSize;
        int end = Math.min((start + pageSize), products.size());
        List<ProductShowDto> pageProduct = products.subList(start, end);
        return new PageImpl<>(pageProduct, PageRequest.of(pageNum, pageSize), products.size());
    }

    @Override
    public Page<ProductShowDto> filterProduct(ProductFilterDto productFilter) {
        if (productFilter.getPageNum() == null) {
            productFilter.setPageNum(0);
        }
        Pageable pageable = PageRequest.of(productFilter.getPageNum(), PRODUCT_DISPLAY_HOME_PER_PAGE, productFilter.getOrderBy());
        Page<ProductCustom> productCustomPage =
                productRepository.filterProduct(productFilter.getKeyword(),
                        productFilter.getGender(), productFilter.getBrandList(),
                        productFilter.getSizeList(), productFilter.getMinPrice(),
                        productFilter.getMaxPrice(), pageable);
        return convertProductCustom(pageable, productCustomPage);
    }

    @Override
    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("Không tìm thấy sản phẩm có mã là " + productId));
    }

    @Override
    public Product saveProduct(Product product) {
        boolean isUpdating = product.getId() != null;

        if (isUpdating) {
            Product productDB = productRepository.findById(product.getId()).get();
            product.setQuantity(productDB.getQuantity());
            product.setStatus(productDB.getStatus());
        }

        return productRepository.save(product);

    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    private Page<ProductShowDto> convertProductCustom(Pageable pageable,
                                                      Page<ProductCustom> productCustomPage) {
        List<ProductCustom> productCustomList = productCustomPage.stream().toList();

        List<ProductShowDto> productShowDtos = new ArrayList<>();
        for (ProductCustom productCustom : productCustomList) {
            ProductShowDto productShowDto = new ProductShowDto(
                    productCustom.getProductId(),
                    productCustom.getProductName(),
                    productCustom.getProductImage(),
                    productCustom.getPrice(),
                    productCustom.getDiscount(),
                    productCustom.getCreatedDate(),
                    productCustom.getStatus()
            );
            productShowDtos.add(productShowDto);
        }
        return new PageImpl<>(productShowDtos, pageable, productCustomPage.getTotalElements());
    }
}
