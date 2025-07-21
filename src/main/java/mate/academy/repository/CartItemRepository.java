package mate.academy.repository;

import java.util.Optional;
import mate.academy.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByShoppingCart_User_IdAndBook_Id(Long userId, Long bookId);

    Optional<CartItem> findByIdAndShoppingCart_Id(Long cartItemId, Long shoppingCartId);
}
