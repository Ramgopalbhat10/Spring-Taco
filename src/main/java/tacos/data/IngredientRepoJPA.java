package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.domain.Ingredient;

public interface IngredientRepoJPA extends CrudRepository<Ingredient, String> {

}
