package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.ermolaayyyyyyy.leschats.presentation.controllers.OwnerServiceController;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.OwnerDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.UserDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.implementations.UserService;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces.CatService;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces.OwnerService;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@WebMvcTest(OwnerServiceController.class)
@ExtendWith(SpringExtension.class)
public class OwnerControllersTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OwnerService ownerService;

    @MockBean
    private CatService catService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    public void givenId_whenGetNotExistingPerson_thenStatus404anExceptionThrown() throws Exception {
        Mockito.when(ownerService.findOwnerById(1)).
                thenReturn(null);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/1"))
                .andExpect(status().isNotFound());
    }
}

