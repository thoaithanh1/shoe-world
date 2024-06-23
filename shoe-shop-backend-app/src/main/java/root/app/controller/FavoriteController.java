package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.app.dto.FavoriteDto;
import root.app.dto.ProductShowDto;
import root.app.entity.Favorite;
import root.app.service.FavoriteService;

@RestController
@RequestMapping("/api/v1/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPageFavoriteByUser(@PathVariable("userId") Long userId,
                                                   @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum) {
        Page<ProductShowDto> favorite = favoriteService.findPageFavoriteByUser(pageNum, userId);
        return ResponseEntity.ok(favorite);
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<?> countProductInFavoriteByUser(@PathVariable("userId") Long userId) {
        int countProduct = favoriteService.countProductInFavoriteByUser(userId);
        return ResponseEntity.ok(countProduct);
    }

    @PostMapping
    public ResponseEntity<?> saveFavorite(@RequestBody FavoriteDto favorite) {
        Favorite saveFavorite = favoriteService.saveFavorite(favorite);
        return ResponseEntity.ok(saveFavorite);
    }

    @DeleteMapping("/{userId}/{productDetailId}")
    public ResponseEntity<?> deleteProductInFavoriteByUser(@PathVariable("userId") Long userId,
                                                           @PathVariable("productDetailId") Long productDetailId) {
        int result = favoriteService.deleteProductInFavoriteByUser(userId, productDetailId);
        return ResponseEntity.ok(result);
    }
}
