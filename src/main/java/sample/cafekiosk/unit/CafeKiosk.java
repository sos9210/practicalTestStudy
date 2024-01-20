package sample.cafekiosk.unit;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {
    private final List<Beverage> beverageList = new ArrayList<>();
    public void add(Beverage beverage) {
        beverageList.add(beverage);
    }

    public void remove(Beverage beverage) {
        beverageList.remove(beverage);
    }

    public void clear() {
        beverageList.clear();
    }
    public int calculateTotalPrice() {
        return beverageList.stream().map(beverage -> beverage.getPrice()).reduce(0, Integer::sum);
    }

    public Order createOrder() {
        return new Order(LocalDateTime.now(), beverageList);
    }
}
