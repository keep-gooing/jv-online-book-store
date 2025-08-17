package mate.academy.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.dao.order.OrderItemResponseDto;
import mate.academy.dao.order.OrderRequestDto;
import mate.academy.dao.order.OrderResponseDto;
import mate.academy.dao.order.OrderUpdateDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.exception.OrderProcessingException;
import mate.academy.mapper.OrderItemMapper;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.ShoppingCart;
import mate.academy.repository.OrderItemRepository;
import mate.academy.repository.OrderRepository;
import mate.academy.repository.ShoppingCartRepository;
import mate.academy.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public OrderResponseDto createOrder(Long userId, OrderRequestDto orderRequestDto) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Shopping cart with user id "
                        + userId + " not found")
        );
        if (cart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("You can't place an order with empty shopping cart");
        }
        Order order = prepareOrder(cart, orderRequestDto);
        orderRepository.save(order);
        cart.getCartItems().clear();
        shoppingCartRepository.save(cart);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrders(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getItemByOrderIdAndItemId(Long userId, Long orderId, Long itemId) {
        OrderItem item = orderItemRepository
                .findByIdAndOrder_IdAndOrder_User_Id(itemId, orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order item by "
                        + "order id " + orderId + ", item id " + itemId + " and user id "
                        + userId));
        return orderItemMapper.toDto(item);
    }

    @Override
    public List<OrderItemResponseDto> getItemsByOrderId(Long userId, Long orderId) {
        return orderItemRepository.findByOrder_IdAndOrder_User_Id(orderId, userId).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderUpdateDto updateDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find order by id " + orderId));
        order.setStatus(updateDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    private Order prepareOrder(ShoppingCart cart, OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        Set<OrderItem> orderItemSet = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemMapper.toOrderItem(cartItem);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());
        order.setOrderItems(orderItemSet);
        BigDecimal total = cart.getCartItems().stream()
                .map(cartItem -> cartItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
        return order;
    }
}
