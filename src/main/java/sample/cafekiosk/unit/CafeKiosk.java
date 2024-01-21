package sample.cafekiosk.unit;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {

    public static final LocalTime SHOP_OPEN_TIME = LocalTime.of(10,0);
    public static final LocalTime SHOP_CLOSE_TIME = LocalTime.of(22,0);

    private final List<Beverage> beverageList = new ArrayList<>();
    public void add(Beverage beverage) {
        beverageList.add(beverage);
    }
    public void add(Beverage beverage, int count) {
        //경계값 테스트
        if(count <= 0) {
            throw new IllegalArgumentException("음료는 1잔 이상 주문하실 수 있습니다.");
        }
        for (int i = 0; i < count; i++) {
            beverageList.add(beverage);
        }
    }

    public void remove(Beverage beverage) {
        beverageList.remove(beverage);
    }

    public void clear() {
        beverageList.clear();
    }
//    public int calculateTotalPrice() {
//        return beverageList.stream().map(beverage -> beverage.getPrice()).reduce(0, Integer::sum);
//    }
    public int calculateTotalPrice() {
        //테스트주도개발(TDD)

        //RED : 실패하는 케이스 먼저작성
        //return 0;

        //GREEN : 빠른시간내에 테스트 성공을 위해 최소한의 코딩
        //return 8500;

        //REFACTOR : 코드를 개선하여 테스트 통과
        return beverageList.stream().mapToInt(Beverage::getPrice).sum();
    }
    public Order createOrder() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalTime currentTime = currentDateTime.toLocalTime();
        if(currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하세요");
        }

        return new Order(LocalDateTime.now(), beverageList);
    }
    public Order createOrder(LocalDateTime currentDateTime) {
        LocalTime currentTime = currentDateTime.toLocalTime();
        if(currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하세요");
        }

        return new Order(LocalDateTime.now(), beverageList);
    }
}
