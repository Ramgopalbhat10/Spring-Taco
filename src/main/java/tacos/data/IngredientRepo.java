package tacos.data;

import tacos.domain.Ingredient;

public interface IngredientRepo {

  Iterable<Ingredient> findAll();

  Ingredient findOne(String id);

  Ingredient save(Ingredient ingredient);
}
