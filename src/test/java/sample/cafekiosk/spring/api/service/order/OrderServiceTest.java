package sample.cafekiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.order.reqeust.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.reponse.OrderResponse;
import sample.cafekiosk.spring.domain.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
//@DataJpaTest
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderService orderService;

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1,product2,product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                            .productNumbers(List.of("001", "002"))
                            .build();

        //when
        OrderResponse orderResponse = orderService
                .createOrder(request,registeredDateTime);

        //then
        Assertions.assertThat(orderResponse.getId()).isNotNull();
        Assertions.assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime,4000);
        Assertions.assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        Assertions.tuple("001",1000),
                        Assertions.tuple("002",3000)
                );
    }

    private Product createProduct(ProductType type,String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("메뉴 이름")
                .price(price)
                .build();
    }
}