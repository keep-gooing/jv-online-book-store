package mate.academy.mapper;

import java.util.Set;
import mate.academy.config.MapperConfig;
import mate.academy.dao.shoppingcart.CartItemDto;
import mate.academy.dao.shoppingcart.CartItemResponseDto;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemResponseDto toDto(CartItem cartItem);

    Set<CartItemResponseDto> toDto(Set<CartItem> cartItems);

    CartItem toEntity(CartItemDto cartItemDto);

    default CartItem toEntity(Book book, ShoppingCart cart, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setShoppingCart(cart);
        cartItem.setQuantity(quantity);
        return cartItem;
    }
}
