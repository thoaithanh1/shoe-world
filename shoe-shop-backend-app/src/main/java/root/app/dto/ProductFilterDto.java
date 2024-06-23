package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterDto {

    private Integer pageNum;

    private String keyword;

    private Integer gender;

    private List<Long> brandList;

    private List<Long> sizeList;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private String orderBy;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public List<Long> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Long> brandList) {
        this.brandList = brandList;
    }

    public List<Long> getSizeList() {
        return sizeList;
    }

    public void setSizeList(List<Long> sizeList) {
        this.sizeList = sizeList;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Sort getOrderBy() {
        switch (this.orderBy) {
            case "min-to-max" -> {
                return Sort.by(Sort.Order.asc("price"));
            }
            case "max-to-min" -> {
                return Sort.by(Sort.Order.desc("price"));
            }
            default -> {
                return Sort.by(Sort.Order.desc("createdDate"));
            }
        }
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
