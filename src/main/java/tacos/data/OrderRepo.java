package tacos.data;

import tacos.domain.Order;

public interface OrderRepo {

  Order save(Order order);
}
