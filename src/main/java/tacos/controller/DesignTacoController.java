package tacos.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.data.JdbcIngredientRepo;
import tacos.data.JdbcTacoRepo;
import tacos.domain.Ingredient;
import tacos.domain.Ingredient.Type;
import tacos.domain.Order;
import tacos.domain.Taco;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("/order")
public class DesignTacoController {

  private final JdbcIngredientRepo ingredientRepo;

  private JdbcTacoRepo tacoRepo;

  @Autowired
  public DesignTacoController(JdbcIngredientRepo ingredientRepo, JdbcTacoRepo tacoRepo) {
    this.ingredientRepo = ingredientRepo;
    this.tacoRepo = tacoRepo;
  }

  @ModelAttribute(name = "order")
  public Order order() {
    return new Order();
  }

  @ModelAttribute(name = "taco")
  public Taco taco() {
    return new Taco();
  }

  @GetMapping
  public String showDesignForm(Model model) {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredientRepo.findAll().forEach(ingredients::add);

    Type[] types = Ingredient.Type.values();
    for (Type type : types) {
      model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
    }

    model.addAttribute("taco", new Taco());

    return "design";
  }

  @PostMapping
  public String processDesign(@Valid Taco taco, Errors errors, Model model, @ModelAttribute Order order) {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredientRepo.findAll().forEach(ingredients::add);

    Type[] types = Ingredient.Type.values();
    for (Type type : types) {
      model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
    }

    model.addAttribute("taco", new Taco());

    if (errors.hasErrors()) {
      return "design";
    }

    Taco saved = tacoRepo.save(taco);
    order.addDesign(taco);

    return "redirect:/orders/current";
  }

  private List<Ingredient> filterByType(List<Ingredient> ingredients, Ingredient.Type type) {
    return ingredients.stream().filter(i -> i.getType().equals(type)).collect(Collectors.toList());
  }

}
