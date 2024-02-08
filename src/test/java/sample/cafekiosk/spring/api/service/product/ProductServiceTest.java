package sample.cafekiosk.spring.api.service.product;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {

        //@BeforeEach를 사용하는것은
        // 각 테스트 입장에서 봤을 때 : 아예 몰라도 테스트 내용을 이해하는데 문제가 없는가?
        // 수정해도 모든 테스트에 영향을 주지 않는가?
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }
    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    void createProduct() {
        //given
        Product product1 = createProduct("001",ProductType.HANDMADE, ProductSellingStatus.SELLING,"아메리카노", 4000);
        productRepository.save(product1);

        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        //when
        ProductResponse productResponse = productService.createProduct(request);

        //then
        Assertions.assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .contains("002", ProductType.HANDMADE,ProductSellingStatus.SELLING,5000,"카푸치노");

        List<Product> products = productRepository.findAll();
        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001",ProductType.HANDMADE,ProductSellingStatus.SELLING,4000,"아메리카노"),
                        Tuple.tuple("002",ProductType.HANDMADE,ProductSellingStatus.SELLING,5000,"카푸치노")
                );

    }

    @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
    @Test
    void createProductWhenProductsIsEmpty() {
        //given
        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        //when
        ProductResponse productResponse = productService.createProduct(request);

        //then
        Assertions.assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .contains("001", ProductType.HANDMADE,ProductSellingStatus.SELLING,5000,"카푸치노");

        List<Product> products = productRepository.findAll();
        Assertions.assertThat(products).hasSize(1)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .contains(
                        Tuple.tuple("001",ProductType.HANDMADE,ProductSellingStatus.SELLING,5000,"카푸치노")
                        );
    }

    private Product createProduct(String productNumber,
                                  ProductType productType,
                                  ProductSellingStatus sellingStatus,
                                  String name,
                                  int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}