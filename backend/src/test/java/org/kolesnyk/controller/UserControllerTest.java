package org.kolesnyk.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kolesnyk.facade.BookingFacade;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.UserImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    public static final String CREATE_USER = "/user/create";
    public static final String USER_BY_ID = "/user/0";
    public static final String FIND_BY_EMAIL = "/user/find/email";
    public static final String FIND_BY_NAME = "/user/find/";

    private static final String VIEW_USER = "user";
    private static final String VIEW_USERS = "users";

    private User user = new UserImpl();
    private final List<User> userList = Arrays.asList(user);

    @Mock
    private BookingFacade bookingFacade;

    @InjectMocks
    private UserController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
        user.setId(0);
    }

    @Test
    void updateUserShouldReturnVoid() throws Exception {
        when(bookingFacade.updateUser(any(User.class))).thenReturn(user);
        mockMvc.perform(put(USER_BY_ID)
                        .param("name", "aaa")
                        .param("email", "a@a"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserShouldReturnVoid() throws Exception {
        when(bookingFacade.deleteUser(anyLong())).thenReturn(true);
        mockMvc.perform(delete(USER_BY_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void createUserShouldReturnVoid() throws Exception {
        when(bookingFacade.createUser(any(User.class))).thenReturn(user);
        mockMvc.perform(post(CREATE_USER)
                        .param("name", "aaa")
                        .param("email", "a@a"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUserByIdShouldReturnUser() throws Exception {
        when(bookingFacade.getUserById(anyLong())).thenReturn(user);
        mockMvc.perform(get(USER_BY_ID))
                .andExpect(status().isOk())
                .andExpect(model().attribute(VIEW_USER, user))
                .andExpect(view().name(VIEW_USER));
    }

    @Test
    void getUserByEmailShouldReturnUser() throws Exception {
        when(bookingFacade.getUserByEmail(anyString())).thenReturn(user);
        mockMvc.perform(get(FIND_BY_EMAIL)
                        .param("email", "a@a"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(VIEW_USER, user))
                .andExpect(view().name(VIEW_USER));
    }

    @Test
    void getUsersByNameShouldReturnList() throws Exception {
        when(bookingFacade.getUsersByName(anyString(), anyInt(), anyInt())).thenReturn(userList);
        mockMvc.perform(get(FIND_BY_NAME).param("name", "AAA")
                        .param("size", "2")
                        .param("num", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(VIEW_USERS, userList))
                .andExpect(view().name(VIEW_USERS));
    }
}
