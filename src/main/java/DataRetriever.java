import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataRetriever {


    Dish findDishById(Integer id) {
        try (Connection connection = new DBConnection().getConnection()) {

            PreparedStatement ps = connection.prepareStatement("""
                SELECT id, name, dish_type, price
                FROM dish
                WHERE id = ?
            """);

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Dish not found " + id);
            }

            Dish dish = new Dish();
            dish.setId(rs.getInt("id"));
            dish.setName(rs.getString("name"));
            dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
            dish.setPrice((Double) rs.getObject("price"));

            // ✅ OK
            dish.setIngredients(findIngredientByDishId(id));

            return dish;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    Dish saveDish(Dish toSave) {
        String upsertDishSql = """
            INSERT INTO dish (id, price, name, dish_type)
            VALUES (?, ?, ?, ?::dish_type)
            ON CONFLICT (id) DO UPDATE
            SET name = EXCLUDED.name,
                dish_type = EXCLUDED.dish_type
            RETURNING id
        """;

        try (Connection conn = new DBConnection().getConnection()) {
            conn.setAutoCommit(false);

            Integer dishId;
            try (PreparedStatement ps = conn.prepareStatement(upsertDishSql)) {

                ps.setInt(1,
                        toSave.getId() != null
                                ? toSave.getId()
                                : getNextSerialValue(conn, "dish", "id"));

                if (toSave.getPrice() != null) {
                    ps.setDouble(2, toSave.getPrice());
                } else {
                    ps.setNull(2, Types.DOUBLE);
                }

                ps.setString(3, toSave.getName());
                ps.setString(4, toSave.getDishType().name());

                ResultSet rs = ps.executeQuery();
                rs.next();
                dishId = rs.getInt(1);
            }

            List<DishIngredient> ingredients = toSave.getIngredients();
            detachIngredients(conn, dishId, ingredients);
            attachIngredients(conn, dishId, ingredients);

            conn.commit();
            return findDishById(dishId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private List<DishIngredient> findIngredientByDishId(Integer dishId) {
        List<DishIngredient> result = new ArrayList<>();

        String sql = """
            SELECT i.id, i.name, i.price, di.quantity_required
            FROM dish_ingredient di
            JOIN ingredient i ON di.id_ingredient = i.id
            WHERE di.id_dish = ?
        """;

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dishId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getInt("id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setPrice(rs.getDouble("price"));

                DishIngredient di = new DishIngredient();
                di.setIngredient(ingredient);
                di.setQuantityRequired(rs.getDouble("quantity_required"));

                result.add(di);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private void detachIngredients(Connection conn, Integer dishId,
                                   List<DishIngredient> ingredients) throws SQLException {

        if (ingredients == null || ingredients.isEmpty()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM dish_ingredient WHERE id_dish = ?")) {
                ps.setInt(1, dishId);
                ps.executeUpdate();
            }
            return;
        }

        String inClause = ingredients.stream()
                .map(di -> "?")
                .collect(Collectors.joining(","));

        String sql = """
            DELETE FROM dish_ingredient
            WHERE id_dish = ?
            AND id_ingredient NOT IN (%s)
        """.formatted(inClause);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dishId);
            int index = 2;
            for (DishIngredient di : ingredients) {
                ps.setInt(index++, di.getIngredient().getId());
            }
            ps.executeUpdate();
        }
    }

    private void attachIngredients(Connection conn, Integer dishId,
                                   List<DishIngredient> ingredients) throws SQLException {

        if (ingredients == null || ingredients.isEmpty()) return;

        String sql = """
            INSERT INTO dish_ingredient (id_dish, id_ingredient, quantity_required)
            VALUES (?, ?, ?)
            ON CONFLICT DO NOTHING
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (DishIngredient di : ingredients) {
                ps.setInt(1, dishId);
                ps.setInt(2, di.getIngredient().getId());
                ps.setDouble(3, di.getQuantityRequired());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }


    private int getNextSerialValue(Connection conn, String table, String column)
            throws SQLException {

        String sql = "SELECT nextval(pg_get_serial_sequence(?, ?))";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, table);
            ps.setString(2, column);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }
}
