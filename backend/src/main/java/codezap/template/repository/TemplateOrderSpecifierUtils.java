package codezap.template.repository;

import static codezap.template.domain.QTemplate.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathBuilder;

import codezap.template.domain.Template;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TemplateOrderSpecifierUtils {

    private static final String TEMPLATE = "template";
    private static final PathBuilder<Template> TEMPLATE_PATH = new PathBuilder<>(Template.class, TEMPLATE);
    private static final Map<String, Path<?>> ORDER_FIELDS = Map.of(
            "likesCount", template.likesCount,
            "createdAt", template.createdAt,
            "modifiedAt", template.modifiedAt
    );

    public static OrderSpecifier<?>[] getOrderSpecifier(Sort sort) {
        var orders = new ArrayList<OrderSpecifier<?>>();
        sort.forEach(order -> addOrder(order, orders));

        return orders.toArray(new OrderSpecifier[0]);
    }

    private static void addOrder(Sort.Order order, List<OrderSpecifier<?>> orders) {
        var direction = getOrder(order);
        orders.add(createOrderSpecifier(order.getProperty(), direction));
    }

    private static Order getOrder(Sort.Order order) {
        if (order.isAscending()) {
            return Order.ASC;
        }
        return Order.DESC;
    }

    private static OrderSpecifier createOrderSpecifier(String property, Order direction) {
        if (ORDER_FIELDS.containsKey(property)) {
            return new OrderSpecifier(direction, ORDER_FIELDS.get(property));
        }
        return new OrderSpecifier(direction, TEMPLATE_PATH.get(property));
    }
}
