package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.domain.Taco;

public interface TacoRepoJPA extends CrudRepository<Taco, Long> {

}
