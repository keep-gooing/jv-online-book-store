package mate.academy.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndOrder_IdAndOrder_User_Id(Long id, Long orderId, Long userId);

    List<OrderItem> findByOrder_IdAndOrder_User_Id(Long orderId, Long userId);
}
