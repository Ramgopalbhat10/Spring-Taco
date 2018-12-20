package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.domain.Order;

public interface OrderRepoJPA extends CrudRepository<Order, Long> {

}
