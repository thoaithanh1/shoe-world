package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import root.app.dto.ProductShowClientDto;
import root.app.dto.ProductDetailDto;
import root.app.entity.Product;
import root.app.entity.ProductDetail;
import root.app.entity.ProductImages;
import root.app.exception.ObjectNotFoundException;
import root.app.mapper.CustomMapper;
import root.app.repository.ProductDetailRepository;
import root.app.repository.ProductImageRepository;
import root.app.repository.ProductRepository;
import root.app.repository.SizeRepository;
import root.app.service.ProductDetailService;
import root.app.util.FileUploadUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    private static final String UPLOAD_DIR_PRODUCT = "E:/Angular/shoe-shop-frontend-app/src/assets/image-data/product-image/";

    private static final Integer PRODUCT_DETAIL_PER_PAGE = 5;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository detailRepository;

    @Autowired
    private ProductImageRepository imageRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private CustomMapper customMapper;

    @Override
    public List<ProductDetailDto> getListProductDetail() {
        return detailRepository.findAll()
                .stream()
                .map(p -> customMapper.convertProductDetailToDto(p))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDetailDto> getListProductDetailByPage(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PRODUCT_DETAIL_PER_PAGE).withSort(Sort.by("product.name").ascending());
        Page<ProductDetail> productDetailsPage = detailRepository.findAll(pageable);
        List<ProductDetailDto> productDetailsList = detailRepository.findAll(pageable)
                .stream()
                .map(p -> customMapper.convertProductDetailToDto(p))
                .collect(Collectors.toList());
        return new PageImpl<>(productDetailsList, pageable, productDetailsPage.getTotalElements());
    }

    @Override
    public ProductDetail findProductDetailById(Long productDetailId) {
        return detailRepository.findById(productDetailId)
                .orElseThrow(() -> new ObjectNotFoundException("Không tìm thấy sản phẩm có mã " + productDetailId));
    }

    @Override
    public ProductShowClientDto findProductDetailAndImage(Long productDetailId) {
        ProductShowClientDto product = productRepository.findProductDetailById(productDetailId);
        product.setProductDetails(sizeRepository.findSizesByProductAndColor(product.getProductId(), product.getColorId()));
        product.setProductImages(imageRepository.findImagesByProductDetail(productDetailId));
        return product;
    }

    @Override
    public ProductDetail findProductDetailByProductAndSize(Long productId, Long sizeId) {
        return detailRepository.findProductDetailByProductAndSize(productId, sizeId);
    }

    @Override
    public ProductDetail saveProductDetail(ProductDetail productDetail, MultipartFile multipartFile, MultipartFile[] subImages) throws IOException {
        Optional<Product> productDB = productRepository.findById(productDetail.getProduct().getId());
        boolean isUpdating = productDetail.getId() != null;

        if (isUpdating) {
            ProductDetail productDetailDB = detailRepository.findById(productDetail.getId()).get();
            productDetail.setSold(productDetailDB.getSold());
            productDetail.setDiscount(productDetailDB.getDiscount());
            productDetail.setCreatedDate(productDetailDB.getCreatedDate());
        }

        if (productDB.isPresent()) {
            Product saveProduct = productDB.get();
            saveProduct.setQuantity(saveProduct.getQuantity() + productDetail.getQuantity());
            productRepository.save(saveProduct);
        }

        if (!multipartFile.isEmpty()) {
            String fileName = multipartFile.getOriginalFilename();
            productDetail.setMainImage(fileName);
            ProductDetail saveProductDetail = detailRepository.save(productDetail);

            String uploadDir = UPLOAD_DIR_PRODUCT + saveProductDetail.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

            addSubImageProduct(saveProductDetail, subImages);
            return saveProductDetail;
        } else {
            if (productDetail.getMainImage() == null) productDetail.setMainImage(null);
            return detailRepository.save(productDetail);
        }

    }

    @Override
    public void deleteProductDetail(Long productDetailId) {
        detailRepository.deleteById(productDetailId);
    }

    private void addSubImageProduct(ProductDetail product, MultipartFile[] subImage) throws IOException {
        for (int i = 0; i < subImage.length; i++) {
            String fileName = subImage[i].getOriginalFilename();
            ProductImages productImages = new ProductImages();
            productImages.setName(fileName);
            productImages.setProductDetail(product);
            ProductImages saveProductImage = imageRepository.save(productImages);

            String uploadDir = UPLOAD_DIR_PRODUCT + product.getId() + "/" + saveProductImage.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, subImage[i]);
        }
    }
}
