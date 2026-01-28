import java.time.Instant;

public class Main {

    public static void main(String[] args) {

        DataRetriever dr = new DataRetriever();

        System.out.println("test plat");

        Dish salade = dr.findDishById(1);
        System.out.println("Plat : " + salade.getName());
        System.out.println("Coût : " + salade.getDishCost());
        System.out.println("Marge : " + salade.getGrossMargin());
        System.out.println();

        Dish poulet = dr.findDishById(2);
        System.out.println("Plat : " + poulet.getName());
        System.out.println("Coût : " + poulet.getDishCost());
        System.out.println("Marge : " + poulet.getGrossMargin());
        System.out.println();

        Dish gateau = dr.findDishById(4);
        System.out.println("Plat : " + gateau.getName());
        System.out.println("Coût : " + gateau.getDishCost());
        System.out.println("Marge : " + gateau.getGrossMargin());
        System.out.println();

        System.out.println("test stock");

        Ingredient laitue = dr.findIngredientById(1);
        Ingredient tomate = dr.findIngredientById(2);
        Ingredient pouletIng = dr.findIngredientById(3);
        Ingredient chocolat = dr.findIngredientById(4);
        Ingredient beurre = dr.findIngredientById(5);

        Instant t = Instant.parse("2024-01-06T12:00:00Z");

        System.out.println("Stock Laitue   : " + laitue.getStockValueAt(t).getQuantity());
        System.out.println("Stock Tomate   : " + tomate.getStockValueAt(t).getQuantity());
        System.out.println("Stock Poulet   : " + pouletIng.getStockValueAt(t).getQuantity());
        System.out.println("Stock Chocolat : " + chocolat.getStockValueAt(t).getQuantity());
        System.out.println("Stock Beurre   : " + beurre.getStockValueAt(t).getQuantity());
    }
}
