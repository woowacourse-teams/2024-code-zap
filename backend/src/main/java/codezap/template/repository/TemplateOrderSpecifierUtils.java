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
    private static final String LIKES_COUNT_PROPERTIES = "likesCount";
    private static final String CREATED_AT_PROPERTIES = "createdAt";
    private static final String MODIFIED_AT_PROPERTIES = "modifiedAt";

    private static final Map<String, Path<?>> TEMPLATE_ORDER_FIELDS = Map.of(
            LIKES_COUNT_PROPERTIES, template.likesCount,
            CREATED_AT_PROPERTIES, template.createdAt,
            MODIFIED_AT_PROPERTIES, template.modifiedAt
    );

    public static OrderSpecifier<?>[] getOrderSpecifier(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        PathBuilder<Template> entityPath = new PathBuilder<>(Template.class, TEMPLATE);
        sort.forEach(order -> addOrder(order, orders, entityPath));

        return orders.toArray(new OrderSpecifier[0]);
    }

    private static void addOrder(Sort.Order order, List<OrderSpecifier<?>> orders, PathBuilder<Template> entityPath) {
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
        orders.add(getPropertyOrderSpecifier(order.getProperty(), direction, entityPath));
    }

    private static OrderSpecifier getPropertyOrderSpecifier(String property, Order direction, PathBuilder<Template> entityPath) {
        Path<?> path = TEMPLATE_ORDER_FIELDS.get(property);

        if (path != null) {
            return new OrderSpecifier(direction, path);
        }

        return new OrderSpecifier(direction, entityPath.get(property));
    }
}
