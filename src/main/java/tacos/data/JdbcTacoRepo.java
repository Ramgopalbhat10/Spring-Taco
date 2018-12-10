package tacos.data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tacos.domain.Ingredient;
import tacos.domain.Taco;

@Repository
public class JdbcTacoRepo implements TacoRepo {

  private JdbcTemplate jdbc;

  @Autowired
  public JdbcTacoRepo(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public Taco save(Taco taco) {
    long tacoId = saveTacoInfo(taco);
    taco.setId(tacoId);
    for (Ingredient ingredient : taco.getIngredients()) {
      saveIngredientToTaco(tacoId, ingredient);
    }
    return null;
  }

  private void saveIngredientToTaco(long tacoId, Ingredient ingredient) {
    jdbc.update("inset into Taco_Ingredients (taco, ingredient) values (?, ?)", tacoId, ingredient.getId());
  }

  private long saveTacoInfo(Taco taco) {

    taco.setCreatedAt(new Date());

    PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
        "insert into Taco (name, createdAt) values (?, ?)", Types.VARCHAR, Types.TIMESTAMP)
            .newPreparedStatementCreator(Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedAt().getTime())));

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbc.update(psc, keyHolder);

    return keyHolder.getKey().longValue();
  }

}
