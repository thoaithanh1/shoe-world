package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import root.app.dto.FavoriteDto;
import root.app.dto.ProductShowDto;
import root.app.entity.Favorite;
import root.app.entity.ProductDetail;
import root.app.entity.User;
import root.app.repository.FavoriteRepository;
import root.app.repository.ProductDetailRepository;
import root.app.repository.UserRepository;
import root.app.service.FavoriteService;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private static final Integer FAVORITE_PER_PAGE = 8;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductDetailRepository detailRepository;

    @Override
    public Page<ProductShowDto> findPageFavoriteByUser(Integer pageNum, Long userId) {
        Pageable pageable = PageRequest.of(pageNum, FAVORITE_PER_PAGE);
        return favoriteRepository.findPageFavoriteByUser(userId, pageable);
    }

    @Override
    public int countProductInFavoriteByUser(Long userId) {
        return favoriteRepository.countProductInFavoriteByUser(userId);
    }

    @Override
    public Favorite saveFavorite(FavoriteDto favoriteDto) {
        User user = userRepository.findById(favoriteDto.getUserId()).get();
        ProductDetail productDetail = detailRepository.findById(favoriteDto.getProductDetailId()).get();
        Favorite favorite = new Favorite(user, productDetail);
        return favoriteRepository.save(favorite);
    }

    @Override
    public int deleteProductInFavoriteByUser(Long userId, Long productDetailId) {
        return favoriteRepository.deleteProductInFavoriteByUser(userId, productDetailId);
    }
}
