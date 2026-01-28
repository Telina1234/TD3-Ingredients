public class StockValue {
    private Double quantity;
    private String unit;

    public StockValue(Double quantity, String unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
