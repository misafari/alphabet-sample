package com.alefba.sample.controller;

import com.alefba.sample.model.Role;
import com.alefba.sample.model.RoleName;
import com.alefba.sample.model.User;
import com.alefba.sample.service.UserService;
import com.alefba.sample.util.exception.RecordNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserService userService;
    @MockBean
    private UserDetailsService userSecurityService;

    @Nested
    @DisplayName("AuthControllerTest SignUp")
    class AuthControllerTest_SignUp {
        @Test
        void ok() throws Exception {
            var returnUser = new User();
            returnUser.setUsername("test-user");
            returnUser.setPassword("123456789");

            given(userService.save(any())).willReturn(returnUser);

            mockMvc.perform(
                            post("%s%s".formatted(UserController.ROOT_PATH, "/sign-up"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                            {
                                                "username": "test-user",
                                                "password": "123456789"
                                            }
                                            """)
                    ).andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void failed_UsernameIsRequired() throws Exception {
            mockMvc.perform(
                            post("%s%s".formatted(UserController.ROOT_PATH, "/sign-up"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                            {
                                                "username": "",
                                                "password": "123456789"
                                            }
                                            """)
                    ).andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("validation.username").value("Username must longer than 3 character"));
        }
    }

    @Nested
    @DisplayName("AuthControllerTest SignIn")
    class AuthControllerTest_SignIn {
        @Test
        void getToken_ok() throws Exception {
            given(userSecurityService
                    .loadUserByUsername("test-user"))
                    .willReturn(
                            new User("test-user",
                                    passwordEncoder.encode("123456789"),
                                    Set.of(new Role(RoleName.ROLE_USER)))
                    );

            mockMvc.perform(
                            post("%s%s".formatted(UserController.ROOT_PATH, "/sign-in"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                            {
                                                "username": "test-user",
                                                "password": "123456789"
                                            }
                                            """)
                    ).andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("token").isString())
                    .andExpect(jsonPath("username").value("test-user"))
                    .andExpect(jsonPath("roles[0]").value("ROLE_USER"));
        }

        @Test
        void getToken_NotAuthorized() throws Exception {
            given(userSecurityService
                    .loadUserByUsername("test-user"))
                    .willThrow(RecordNotFoundException.class);

            mockMvc.perform(
                            post("%s%s".formatted(UserController.ROOT_PATH, "/sign-in"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                            {
                                                "username": "test-user",
                                                "password": "123456789"
                                            }
                                            """)
                    ).andDo(print())
                    .andExpect(status().isUnauthorized());
        }
    }
}