package mate.academy.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.dao.shoppingcart.ShoppingCartResponseDto;
import mate.academy.dao.shoppingcart.UpdateCartItemRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CartItemMapper;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.BookRepository;
import mate.academy.repository.CartItemRepository;
import mate.academy.repository.ShoppingCartRepository;
import mate.academy.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartForCurrentUser(Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping "
                        + "cart not found for user id " + userId));
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartResponseDto addItemToCart(Long userId, Long bookId, int quantity) {
        ShoppingCart currentShoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping"
                        + "cart not found for user id " + userId));
        CartItem currentCartItem = cartItemRepository
                .findCartItemByShoppingCart_User_IdAndBook_Id(userId, bookId);
        if (currentCartItem == null) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("Can't find book by id "
                            + bookId));
            currentCartItem = cartItemMapper.toEntity(book, currentShoppingCart, quantity);
        } else {
            currentCartItem.setQuantity(currentCartItem.getQuantity() + quantity);
        }
        cartItemRepository.save(currentCartItem);
        currentCartItem.setShoppingCart(currentShoppingCart);
        return shoppingCartMapper.toDto(currentShoppingCart);
    }

    @Override
    public ShoppingCartResponseDto updateCartItemQuantity(Long itemId,
                                                      User user,
                                                      UpdateCartItemRequestDto cartItemDto) {
        ShoppingCart currentShoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping"
                        + "cart not found for user id " + user.getId()));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(itemId, user.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find item by id " + itemId));
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(currentShoppingCart);
    }

    @Override
    public ShoppingCartResponseDto deleteCartItem(User user, Long itemId) {
        ShoppingCart currentShoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping"
                        + "cart not found for user id " + user.getId()));
        cartItemRepository.deleteCartItemByIdAndShoppingCartId(itemId, user.getId());
        return shoppingCartMapper.toDto(currentShoppingCart);
    }
}
