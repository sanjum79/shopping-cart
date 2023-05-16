package de.obi.demo.cart;

import de.obi.demo.cart.repositories.CartRepository;
import de.obi.demo.cart.services.ShoppingCartService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingCartApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    public void setup() {

//        doNothing().when(shoppingCartService).addItemToCart(1L, 1L, 5);

//        Cart cart = new Cart(1L, new ArrayList<>());
//        when(cartRepository.findById(1L))
//                .thenReturn(Optional.of(cart));

        initMocks(this); //without this you will get NPE
    }

    @Test
    void shouldReturnCreatedMessage() throws Exception {
        this.mockMvc.perform(post("/api/carts/1/items?productId=2&quantity=2"))
                .andExpect(status().isCreated())
                .andExpect(content().string(CoreMatchers.containsString("Item successfully added to cart.")));
    }

    @Test
    void shouldReturnDeletionMessage() throws Exception {
        this.mockMvc.perform(delete("/api/carts/items/4"))
                .andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("Item removed from cart successfully")));
    }

    @Test
    void shouldReturnCartItems() throws Exception {
        this.mockMvc.perform(get("/api/carts/2/items"))
                .andExpect(status().isOk());
    }
}
