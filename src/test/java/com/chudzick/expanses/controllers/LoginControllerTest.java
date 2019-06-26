package com.chudzick.expanses.controllers;

import com.chudzick.expanses.TestUserSupplier;
import com.chudzick.expanses.domain.UserDto;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration
public class LoginControllerTest implements TestUserSupplier {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldNotRegisterUserWithTooShortLogin() throws Exception {
        UserDto userDto = prepareValidUserDto();
        userDto.setLogin("short");

        sendRequestWithNotValidUserAndCheckModelResponse(userDto);
    }

    @Test
    @Ignore
    public void shouldNotRegisterUserWithNoMatchingPasswords() throws Exception {
        UserDto userDto = prepareValidUserDto();
        userDto.setPassword("NO_MATCH");

        sendRequestWithNotValidUserAndCheckModelResponse(userDto);
    }

    @Test
    public void shouldNotRegisterUserWithEmptyName() throws Exception {
        UserDto userDto = prepareValidUserDto();
        userDto.setName("");

        sendRequestWithNotValidUserAndCheckModelResponse(userDto);
    }

    @Test
    public void shouldNotRegisterUserWithEmptyLastName() throws Exception {
        UserDto userDto = prepareValidUserDto();
        userDto.setLastName("");

        sendRequestWithNotValidUserAndCheckModelResponse(userDto);
    }

    @Test
    public void shouldNotRegisterUserWithNotValidEmail() throws Exception {
        UserDto userDto = prepareValidUserDto();

        userDto.setEmail("notValid@a"); // Missing .com
        sendRequestWithNotValidUserAndCheckModelResponse(userDto);

        userDto.setEmail("not.com"); // Missing @
        sendRequestWithNotValidUserAndCheckModelResponse(userDto);
    }

    @Test
    public void shouldRegisterProperUser() throws Exception {
        UserDto userDto = prepareValidUserDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .flashAttr("userDto", userDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bindingResult", nullValue()))
                .andExpect(view().name("register"));
    }

    private void sendRequestWithNotValidUserAndCheckModelResponse(UserDto userDto) throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .flashAttr("userDto", userDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bindingResult", notNullValue()))
                .andExpect(view().name("register"));
    }
}
