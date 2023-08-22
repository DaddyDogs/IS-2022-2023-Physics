package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.ExtensionMethod;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.Owner;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.User;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.models.Role;
import ru.ermolaayyyyyyy.leschats.presentation.controllers.CatServiceController;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.CatDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.UserDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.exceptions.AccessDeniedException;
import ru.ermolaayyyyyyy.leschats.servicelayer.mapping.CatDtoMapping;
import ru.ermolaayyyyyyy.leschats.servicelayer.mapping.UserDtoMapping;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.implementations.UserService;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces.CatService;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces.OwnerService;
import org.springframework.web.context.WebApplicationContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@WebMvcTest(CatServiceController.class)
@ExtendWith(SpringExtension.class)
@ExtensionMethod(UserDtoMapping.class)
@Import(TestConfig.class)
public class CatControllersTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OwnerService ownerService;

    @MockBean
    private CatService catService;

    @MockBean
    private UserService userService;
    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors
                .httpBasic(user.getUsername(), user.getPassword());
    }





    @BeforeEach
    public void setTests(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity()).build();
        CatDto catDto = new CatDto("Ivanka", LocalDate.of(2000, 5, 5), "Russian", "WHITE", 1, new ArrayList<>(), 1);
        given(catService.findCatById(1)).willReturn(catDto);

        CatDto catDto2 = new CatDto("Ivanka", LocalDate.of(2000, 5, 5), "Russian", "WHITE", 1, new ArrayList<>(), 2);
        given(catService.findCatById(2)).willReturn(catDto2);

        List<CatDto> catDtos = new ArrayList<>();
        catDtos.add(catDto);
        catDtos.add(catDto2);
        given(catService.findAllCats()).willReturn(catDtos);
        LocalDate date = LocalDate.now();
        User user = new User(new Owner("Pol", date), "fdfd", "fdfd", Role.ROLE_ADMIN);
        given(userService.loadUserByUsername("user1")).willReturn(user);
        given(userService.findUserById(1)).willReturn(new UserDto(1, 1, "Pol", date));
    }




    @Test
    public void makeFriends_withoutAuthentication_Forbidden() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/friends/1").contentType(MediaType.APPLICATION_JSON).content("2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "p123")
    public void deleteForeignCat_Status200_Forbidden() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/cats/1"))
                .andExpect(status().isForbidden());
    }
}

