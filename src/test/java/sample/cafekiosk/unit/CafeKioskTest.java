package sample.cafekiosk.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

public class CafeKioskTest {

    @Test
    void add_manual_test() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBeverageList().size());
        System.out.println(">>> 담긴 음료 : " + cafeKiosk.getBeverageList().get(0).getName());
    }

    //DisplayName을 작성성할때 떤 문구가 더 이해하기 쉬울지 고민해보자.
    //테스트 행위를 함으로써 어떤 결과가 나오는지 담는것이 더 좋아보인다. ex) 음료 1개 추가하면 주문 목록에 담긴다.
    //@DisplayName("음료 1개 추가 테스트")
    @DisplayName("음료 1개 추가하면 주문 목록에 담긴다.")
    @Test
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        Assertions.assertThat(cafeKiosk.getBeverageList().size()).isEqualTo(1);
        Assertions.assertThat(cafeKiosk.getBeverageList().get(0).getName()).isEqualTo("아메리카노");
    }
    @Test
    void addSeveralBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano,2);

        Assertions.assertThat(cafeKiosk.getBeverageList().get(0)).isEqualTo(americano);
        Assertions.assertThat(cafeKiosk.getBeverageList().get(0)).isEqualTo(americano);
    }
    @Test
    void addZeroBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        Assertions.assertThatThrownBy(() -> cafeKiosk.add(americano,0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
    }

    @Test
    void remove() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Assertions.assertThat(cafeKiosk.getBeverageList()).hasSize(1);

        cafeKiosk.remove(americano);
        Assertions.assertThat(cafeKiosk.getBeverageList()).isEmpty();
    }

    @Test
    void clear() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        Assertions.assertThat(cafeKiosk.getBeverageList()).hasSize(2);

        cafeKiosk.clear();
        Assertions.assertThat(cafeKiosk.getBeverageList()).isEmpty();
    }

    @DisplayName("주문목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
    @Test
    void calculateTotalPrice() {
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        //when
        int totalPrice = cafeKiosk.calculateTotalPrice();

        //then
        Assertions.assertThat(totalPrice).isEqualTo(8500);
    }
    @Test
    void createOrder() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder();
        Assertions.assertThat(order.getBeverageList()).hasSize(1);
        Assertions.assertThat(order.getBeverageList().get(0).getName()).isEqualTo("아메리카노");
    }
    @Test
    void createOrderWithCurrentTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023,1,17,10,0));
        Assertions.assertThat(order.getBeverageList()).hasSize(1);
        Assertions.assertThat(order.getBeverageList().get(0).getName()).isEqualTo("아메리카노");
    }
    @Test
    void createOrderOutsideOpenTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Assertions.assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2023,1,17,9,59)))
                        .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요");
    }


}
