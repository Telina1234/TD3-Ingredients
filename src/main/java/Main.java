import java.time.Instant;

public class Main {
    public static void main(String[] args) {

        DataRetriever dataRetriever = new DataRetriever();

        Order order = new Order();
        order.setReference("CMD-001");
        order.setCreationDatetime(Instant.now());
        order.setType(OrderTypeEnum.EAT_IN);
        order.setStatus(OrderStatusEnum.CREATED);

        Order saved = dataRetriever.saveOrder(order);
        System.out.println(saved);

        saved.setStatus(OrderStatusEnum.READY);
        dataRetriever.saveOrder(saved);
        System.out.println(saved);

        saved.setStatus(OrderStatusEnum.DELIVERED);
        dataRetriever.saveOrder(saved);
        System.out.println(saved);

        saved.setType(OrderTypeEnum.TAKE_AWAY);
        dataRetriever.saveOrder(saved);
    }
}
