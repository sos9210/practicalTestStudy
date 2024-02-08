package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductTypeTest {

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType1() {
        //given
        ProductType givenType = ProductType.HANDMADE;

        //when
        boolean result = ProductType.containsStockType(givenType);

        //then
        Assertions.assertThat(result).isFalse();
    }
    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType2() {
        //given
        ProductType givenType = ProductType.BAKERY;

        //when
        boolean result = ProductType.containsStockType(givenType);

        //then
        Assertions.assertThat(result).isTrue();
    }
    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType3() {
        //given
        ProductType givenType1 = ProductType.HANDMADE;
        ProductType givenType2 = ProductType.BOTTLE;
        ProductType givenType3 = ProductType.BAKERY;

        //when
        boolean result1 = ProductType.containsStockType(givenType1);
        boolean result2 = ProductType.containsStockType(givenType2);
        boolean result3 = ProductType.containsStockType(givenType3);

        //then
        Assertions.assertThat(result1).isFalse();
        Assertions.assertThat(result2).isTrue();
        Assertions.assertThat(result3).isTrue();
    }


    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @CsvSource({"HANDMADE,false", "BOTTLE,true", "BAKERY,true"})  //테스트 메서드의 파라미터에 값으로 들어간다
    @ParameterizedTest          //파라미터값에 변화를 줘서 테스트하고싶은경우
    void containsStockType4(ProductType productType, boolean expected) {
        //when
        boolean result = ProductType.containsStockType(productType);

        //then
        Assertions.assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> provideProductTypesForCheckingStockType() {
        return Stream.of(
                Arguments.of(ProductType.HANDMADE,false),
                Arguments.of(ProductType.BOTTLE,true),
                Arguments.of(ProductType.BAKERY,true)
        );
    }

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @MethodSource("provideProductTypesForCheckingStockType")  //파라미터에값을 넣어줄 메서드명을 작성한다.
    @ParameterizedTest          //파라미터값에 변화를 줘서 테스트하고싶은경우
    void containsStockType5(ProductType productType, boolean expected) {
        //when
        boolean result = ProductType.containsStockType(productType);

        //then
        Assertions.assertThat(result).isEqualTo(expected);
    }

}