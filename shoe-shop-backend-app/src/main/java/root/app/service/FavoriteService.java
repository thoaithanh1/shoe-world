package root.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import root.app.dto.FavoriteDto;
import root.app.dto.ProductShowDto;
import root.app.entity.Favorite;

public interface FavoriteService {

    Page<ProductShowDto> findPageFavoriteByUser(Integer pageNum, Long userId);

    int countProductInFavoriteByUser(Long userId);

    Favorite saveFavorite(FavoriteDto favorite);

    int deleteProductInFavoriteByUser(Long userId, Long productDetailId);
}
