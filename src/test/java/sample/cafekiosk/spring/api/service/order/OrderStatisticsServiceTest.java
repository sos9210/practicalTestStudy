package sample.cafekiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class OrderStatisticsServiceTest extends IntegrationTestSupport {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    //@MockBean을 사용하면 @SpringBootTest가 아님에도 서버가 새로 띄워지는 비용이 발생한다.
    /**
     * 방법1. @MockBean이 적용된 객체를 상위클래스로 이동시키고 접근제어자를 protected로 사용해서 적용한다.
     */    
    /**
     * 방법2. @MockBean을 사용한 방법과 사용하지 않는 방법을 동시에 사용하고싶으면 환경을 2개로 분리하자.
     *  ex ) IntegrationTestSupport와 같은 상위클래스를 2개 생성하여 사용
     */
//    @MockBean
//    private MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 1, 28, 0, 0);

        Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 2000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 3000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2024,1,27,23,59,59), products);
        Order order2 = createPaymentCompletedOrder(now, products);
        Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2024,1,28,23,59,59), products);
        Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2024,1,29,0,0), products);

        //stubing
        //행위를 mock객체에 stubing한다고 표현한다.
        //메일전송에대한 부분이 테스트에 불필요한 과정이기에 mock을 사용한다.
        Mockito.when(mailSendClient.sendEmail(
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(String.class)
                )
        ).thenReturn(true);

        //when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(
                LocalDate.of(2024, 1, 28),
                "test@test.com"
        );

        //then
        Assertions.assertThat(result).isTrue();
        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        Assertions.assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원 입니다.");
    }

    private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(now)
                .build();
        return orderRepository.save(order);
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("메뉴 이름")
                .price(price)
                .build();
    }
}