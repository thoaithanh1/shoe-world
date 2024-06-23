package root.app.contant;

import java.math.BigDecimal;

public enum RankingContant {

    BRONZE(BigDecimal.ZERO, BigDecimal.ZERO),
    SILVER(BigDecimal.valueOf(1_000_000), BigDecimal.valueOf(20000)),
    GOLD(BigDecimal.valueOf(3_000_000), BigDecimal.valueOf(30000)),
    PLATINUM(BigDecimal.valueOf(5_000_000), BigDecimal.valueOf(50000)),
    DIAMOND(BigDecimal.valueOf(10_000_000), BigDecimal.valueOf(80000));

    public BigDecimal condition;

    public BigDecimal discount;

    RankingContant(BigDecimal condition, BigDecimal discount) {
        this.condition = condition;
        this.discount = discount;
    }
}
