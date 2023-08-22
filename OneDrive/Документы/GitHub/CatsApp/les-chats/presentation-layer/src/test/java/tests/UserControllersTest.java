package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.implementations.UserService;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces.CatService;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces.OwnerService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UserControllersTest.class)
@ExtendWith(SpringExtension.class)
public class UserControllersTest {
    @MockBean
    private OwnerService ownerService;

    @MockBean
    private CatService catService;

    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    @Test
    public void tryFindNotExistedOwner_Status404() throws Exception {
        Mockito.when(userService.findUserById(1)).
                thenReturn(null);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isNotFound());
    }
}
