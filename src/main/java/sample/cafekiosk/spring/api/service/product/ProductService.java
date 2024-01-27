package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.dto.request.ProductCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Transactional
 * readOnly = true : 읽기전용
 * CRUD에서 CUD가 동작하지않음. 오직 읽기전용 트랜잭션
 * JPA의 CUD스냅샷 저장, 변경감지가 생기지 않음 (성능향상)
 * CQRS - Command / Query
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    // 클래스레벨에 @Transactional(readOnly = true) 적용
    // CUD작업이 필요한 메서드에만 @Transactional을 붙여주는 방식을 권장
    @Transactional
    public ProductResponse createProduct(ProductCreateServiceRequest request) {
        //productNumber
        //001,002,003,004 ....
        //DB에서 마지막 저장된 Product의 상품번호를 읽어와서 +1
        String nextProductNumber = createNextProductNumber();
        //nextProductNumber
        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);

    }
    @Transactional(readOnly = true)
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }
    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if( latestProductNumber == null ) {
            return "001";
        }
        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumber = latestProductNumberInt + 1;

        return String.format("%03d",nextProductNumber);
    }
}
