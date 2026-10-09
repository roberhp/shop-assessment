package com.liverpool.appsales.exam.infrastructure.seed;

import com.liverpool.appsales.exam.customer.application.CustomerRepository;
import com.liverpool.appsales.exam.customer.domain.Customer;
import com.liverpool.appsales.exam.delivery.application.DeliveryRepository;
import com.liverpool.appsales.exam.delivery.domain.Delivery;
import com.liverpool.appsales.exam.item.application.ItemRepository;
import com.liverpool.appsales.exam.item.domain.Item;
import com.liverpool.appsales.exam.order.application.OrderRepository;
import com.liverpool.appsales.exam.order.domain.Order;
import com.liverpool.appsales.exam.order.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    private final ItemRepository itemRepository;

    private final OrderRepository orderRepository;

    private final DeliveryRepository deliveryRepository;

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;

    @Override
    public void run(String... args) {
        if (!seedEnabled) {
            return;
        }

        seedCustomers();
        seedItems();
        seedOrders();
        seedDeliveries();
    }

    private void seedCustomers() {
        List<Customer> customers = List.of(
                new Customer(
                        "75c97531-abf5-4524-8107-90aa48d08efc",
                        "nombre1",
                        "apellidoPaterno1",
                        "apellidoMaterno1",
                        "correo1@test.com",
                        List.of()
                ),
                new Customer(
                        "aa2ae8bf-6b32-45dc-bb69-14e899bd8fed",
                        "nombre2",
                        "apellidoPaterno2",
                        "apellidoMaterno2",
                        "correo2@test.com",
                        List.of(
                                "20251216366900020031"
                        )
                ),
                new Customer(
                        "b446eb39-84fc-46e2-923b-91771d806c38",
                        "nombre3",
                        "apellidoPaterno3",
                        "apellidoMaterno3",
                        "correo3@test.com",
                        List.of(
                                "20251208711700070401"
                        )
                ),
                new Customer(
                        "93a5c099-3bbe-4718-8a0a-2e1bae0e08c8",
                        "nombre4",
                        "apellidoPaterno4",
                        "apellidoMaterno4",
                        "correo4@test.com",
                        List.of(
                                "20251208711600070401"
                        )
                ),
                new Customer(
                        "f6ec8262-7017-4532-a382-70c86e7844fc",
                        "nombre5",
                        "apellidoPaterno5",
                        "apellidoMaterno5",
                        "correo5@test.com",
                        List.of(
                                "4550129455"
                        )
                ),
                new Customer(
                        "12665a1b2c3d4e5f6a7b8c9d0e",
                        "nombre6",
                        "apellidoPaterno6",
                        "apellidoMaterno6",
                        "correo6@test.com",
                        List.of(
                                "301009163489"
                        )
                ),
                new Customer(
                        "test-user-7",
                        "nombre7",
                        "apellidoPaterno7",
                        "apellidoMaterno7",
                        "correo7@test.com",
                        List.of()
                )
        );

        customers.stream()
                .filter(customer -> !customerRepository.existsByUserId(
                        customer.getUserId()
                ))
                .forEach(customerRepository::save);
    }

    private void seedItems() {
        List<Item> items = List.of(
                new Item(
                        "3010091676-1132351437",
                        "1132351437",
                        3,
                        "PantalÃ³n LeviÂ´s",
                        "Compra en lÃ­nea",
                        "1"
                ),
                new Item(
                        "3010091676-1179743767",
                        "1179743767",
                        4,
                        "Vasos cristal 250ml",
                        "Compra en tienda",
                        "2"
                ),
                new Item(
                        "20251216366900020031-1171500610",
                        "1171500610",
                        2,
                        "Control Xbox one",
                        "Pedido entregado",
                        "3"
                ),
                new Item(
                        "20251216366900020031-898",
                        "898",
                        1,
                        "Play Station 5",
                        "Pedido entregado",
                        "4"
                ),
                new Item(
                        "20251208711700070401-ci55715674037",
                        "ci55715674037",
                        1,
                        "Zelda: Breathe of the Wild",
                        "Compra en lÃ­nea",
                        "5"
                ),
                new Item(
                        "20251208711700070401-ci55712179998",
                        "ci55712179998",
                        1,
                        "Playera cabellero LeviÂ´s",
                        "Compra en tienda",
                        "6"
                ),
                new Item(
                        "20251208711600070401-1179251813",
                        "1179251813",
                        1,
                        "Laptop Lenovo thinkpad",
                        "Pedido entregado",
                        "7"
                ),
                new Item(
                        "20251208711600070401-1150792602",
                        "1150792602",
                        1,
                        "Mouse logitech",
                        "Pedido entregado",
                        "8"
                ),
                new Item(
                        "4550129455-ci55667397890",
                        "ci55667397890",
                        1,
                        "Monitor Acer 24 pulgadas",
                        "Pedido entregado",
                        "9"
                ),
                new Item(
                        "4550129455-1106253214",
                        "1106253214",
                        1,
                        "Teclado logitech",
                        "Compra en lÃ­nea",
                        "10"
                ),
                new Item(
                        "301009163489-5434323412",
                        "5434323412",
                        1,
                        "Celula iPhone 14",
                        "Compra en lÃ­nea",
                        "11"
                )
        );

        items.stream()
                .filter(item -> !itemRepository.existsByItemId(item.getItemId()))
                .forEach(itemRepository::save);
    }

    private void seedOrders() {
        List<Order> orders = List.of(
                new Order(
                        "3010091676",
                        "75c97531-abf5-4524-8107-90aa48d08efc",
                        "online",
                        "2025-12-06",
                        "L  SANTA FE",
                        null,
                        List.of(
                                new OrderItem(
                                        "3010091676-1132351437",
                                        "1132351437",
                                        3
                                ),
                                new OrderItem(
                                        "3010091676-1179743767",
                                        "1179743767",
                                        4
                                )
                        )
                ),
                new Order(
                        "20251216366900020031",
                        "aa2ae8bf-6b32-45dc-bb69-14e899bd8fed",
                        "physical",
                        "2025-12-08",
                        "Liverpool GalerÃ­as Toluca",
                        null,
                        List.of(
                                new OrderItem(
                                        "20251216366900020031-1171500610",
                                        "1171500610",
                                        2
                                ),
                                new OrderItem(
                                        "20251216366900020031-898",
                                        "898",
                                        1
                                )
                        )
                ),
                new Order(
                        "20251208711700070401",
                        "b446eb39-84fc-46e2-923b-91771d806c38",
                        "physical",
                        "2025-11-20",
                        "Liverpool GalerÃ­as SerdÃ¡n",
                        null,
                        List.of(
                                new OrderItem(
                                        "20251208711700070401-ci55715674037",
                                        "ci55715674037",
                                        1
                                ),
                                new OrderItem(
                                        "20251208711700070401-ci55712179998",
                                        "ci55712179998",
                                        1
                                )
                        )
                ),
                new Order(
                        "20251208711600070401",
                        "93a5c099-3bbe-4718-8a0a-2e1bae0e08c8",
                        "physical",
                        "2025-11-10",
                        "Liverpool AngelÃ³polis",
                        null,
                        List.of(
                                new OrderItem(
                                        "20251208711600070401-1179251813",
                                        "1179251813",
                                        1
                                ),
                                new OrderItem(
                                        "20251208711600070401-1150792602",
                                        "1150792602",
                                        1
                                )
                        )
                ),
                new Order(
                        "4550129455",
                        "f6ec8262-7017-4532-a382-70c86e7844fc",
                        "online",
                        "2025-12-05",
                        "Liverpool Parque Puebla",
                        null,
                        List.of(
                                new OrderItem(
                                        "4550129455-ci55667397890",
                                        "ci55667397890",
                                        1
                                ),
                                new OrderItem(
                                        "4550129455-1106253214",
                                        "1106253214",
                                        1
                                )
                        )
                ),
                new Order(
                        "30100916760987",
                        "75c97531-abf5-4524-8107-90aa48d08efc",
                        "online",
                        "2025-12-06",
                        "L  SANTA FE",
                        null,
                        List.of(
                                new OrderItem(
                                        "30100916760987-1132351437",
                                        "1132351437",
                                        3
                                ),
                                new OrderItem(
                                        "30100916760987-1179743767",
                                        "1179743767",
                                        4
                                )
                        )
                ),
                new Order(
                        "632005897",
                        "75c97531-abf5-4524-8107-90aa48d08efc",
                        "online",
                        "2026-05-15",
                        "Monterrey Centro",
                        null,
                        List.of(
                                new OrderItem(
                                        "632005897-749826482",
                                        "749826482",
                                        1
                                )
                        )
                ),
                new Order(
                        "301009163489",
                        "12665a1b2c3d4e5f6a7b8c9d0e",
                        "online",
                        "2025-12-06",
                        "Monterrey Centro",
                        null,
                        List.of(
                                new OrderItem(
                                        "301009163489-5434323412",
                                        "5434323412",
                                        1
                                )
                        )
                )
        );

        orders.stream()
                .filter(order -> !orderRepository.existsByOrderRef(
                        order.getOrderRef()
                ))
                .forEach(orderRepository::save);
    }

    private void seedDeliveries() {
        List<Delivery> deliveries = List.of(
                new Delivery(
                        "delivery-001",
                        "3010091676",
                        "Av. Paseo de la Reforma 123, CDMX"
                ),
                new Delivery(
                        "delivery-002",
                        "20251216366900020031",
                        "Av. Hidalgo 456, Toluca, Estado de México"
                ),
                new Delivery(
                        "delivery-003",
                        "4550129455",
                        "Av. Juárez 789, Puebla, Puebla"
                ),
                new Delivery(
                        "delivery-004",
                        "301009163489",
                        "Av. Constitución 321, Monterrey, Nuevo León"
                )
        );

        deliveries.stream()
                .filter(delivery -> !deliveryRepository.existsByDeliveryId(
                        delivery.getDeliveryId()
                ))
                .forEach(deliveryRepository::save);
    }
}