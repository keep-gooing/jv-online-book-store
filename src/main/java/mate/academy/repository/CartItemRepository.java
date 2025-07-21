package mate.academy.repository;

import java.util.Optional;
import mate.academy.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findCartItemByShoppingCart_User_IdAndBook_Id(Long userId, Long bookId);

    Optional<CartItem> findByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);
}
