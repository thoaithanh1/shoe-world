package root.app.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import root.app.dto.ProductShowDto;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

//    @Test
//    public void getListProductTests() {
//        List<ProductShowDto> products = productRepository.findProductShow();
//        products.forEach(System.out::println);
//    }
}
