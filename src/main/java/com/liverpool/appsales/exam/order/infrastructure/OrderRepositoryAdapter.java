package com.liverpool.appsales.exam.order.infrastructure;

import com.liverpool.appsales.exam.order.application.OrderRepository;
import com.liverpool.appsales.exam.order.domain.Order;
import com.liverpool.appsales.exam.order.domain.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderMongoRepository mongoRepository;

    public OrderRepositoryAdapter(OrderMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Order save(Order order) {

        OrderDocument document = mongoRepository
                .findByOrderRef(order.getOrderRef())
                .orElseGet(OrderDocument::new);

        document.setOrderRef(order.getOrderRef());
        document.setUserId(order.getUserId());
        document.setCanal(order.getCanal());
        document.setStoreName(order.getStoreName());
        document.setEstimateDeliveryDate(order.getEstimateDeliveryDate());
        document.setOrderStatus(order.getOrderStatus());

        List<OrderItemDocument> items = order.getItems()
                .stream()
                .map(this::toDocument)
                .toList();

        document.setItems(items);

        OrderDocument savedDocument = mongoRepository.save(document);

        return toDomain(savedDocument);
    }

    @Override
    public Optional<Order> findByOrderRef(String orderRef) {
        return mongoRepository.findByOrderRef(orderRef)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByOrderRef(String orderRef) {
        return mongoRepository.existsByOrderRef(orderRef);
    }

    @Override
    public List<Order> findAll() {
        return mongoRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Order> findByOrderRefContaining(String orderRef) {
        return mongoRepository.findByOrderRefContainingIgnoreCase(orderRef)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Order> findByOrderStatusContaining(String orderStatus) {
        return mongoRepository.findByOrderStatusContainingIgnoreCase(orderStatus)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Order> findByStoreNameContaining(String storeName) {
        return mongoRepository.findByStoreNameContainingIgnoreCase(storeName)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteByOrderRef(String orderRef) {
        mongoRepository.deleteByOrderRef(orderRef);
    }

    @Override
    public List<Order> findByUserId(String userId) {
        return mongoRepository.findByUserId(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }


    private OrderItemDocument toDocument(OrderItem item) {
        return new OrderItemDocument(
                item.getItemId(),
                item.getSkuId(),
                item.getQuantity());
    }

    private Order toDomain(OrderDocument document) {

        List<OrderItem> items = document.getItems()
                .stream()
                .map(this::toDomain)
                .toList();

        return new Order(
                document.getOrderRef(),
                document.getUserId(),
                document.getCanal(),
                document.getOrderStatus(),
                document.getStoreName(),
                document.getEstimateDeliveryDate(),
                items);
    }

    private OrderItem toDomain(OrderItemDocument document) {
        return new OrderItem(
                document.getItemId(),
                document.getSkuId(),
                document.getQuantity()
        );
    }
}