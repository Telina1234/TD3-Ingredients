public class Main {
    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();

        Dish dish = dr.findDishById(1);
        System.out.println(dish.getDishCost());       // 250
        System.out.println(dish.getGrossMargin());    // 3250
    }
}
