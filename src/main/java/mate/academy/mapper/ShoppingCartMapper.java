package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dao.shoppingcart.CartItemResponseDto;
import mate.academy.dao.shoppingcart.ShoppingCartResponseDto;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class,
        uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItem", source = "cartItems")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    CartItemResponseDto toDto(CartItem cartItem);
}
