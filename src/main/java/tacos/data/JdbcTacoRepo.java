package tacos.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import tacos.domain.Ingredient;
import tacos.domain.Taco;

@Repository
public class JdbcTacoRepo implements TacoRepo {

  private SimpleJdbcInsert tacoInserter;
  private SimpleJdbcInsert tacoIngredientInserter;
  private ObjectMapper objectMapper;

  @Autowired
  public JdbcTacoRepo(JdbcTemplate jdbc) {
    this.tacoInserter = new SimpleJdbcInsert(jdbc).withTableName("Taco").usingGeneratedKeyColumns("id");
    this.tacoIngredientInserter = new SimpleJdbcInsert(jdbc).withTableName("Taco_Ingredients");
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public Taco save(Taco taco) {
    taco.setCreatedAt(new Date());
    long tacoId = saveTacoInfo(taco);
    taco.setId(tacoId);
    for (Ingredient ingredient : taco.getIngredients()) {
      saveIngredientToTaco(tacoId, ingredient);
    }
    return taco;
  }

  private void saveIngredientToTaco(long tacoId, Ingredient ingredient) {
    Map<String, Object> values = new HashMap<>();
    values.put("taco", tacoId);
    values.put("ingredient", ingredient.getId());
    tacoIngredientInserter.execute(values);
  }

  private long saveTacoInfo(Taco taco) {
    @SuppressWarnings("unchecked")
    Map<String, Object> values = objectMapper.convertValue(taco, Map.class);
    values.put("createdAt", taco.getCreatedAt());
    return tacoInserter.executeAndReturnKey(values).longValue();
  }

}
