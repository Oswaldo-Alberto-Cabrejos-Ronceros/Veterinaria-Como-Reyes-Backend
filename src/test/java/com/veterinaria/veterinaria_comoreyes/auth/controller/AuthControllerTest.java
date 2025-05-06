package com.veterinaria.veterinaria_comoreyes.auth.controller;

import com.veterinaria.veterinaria_comoreyes.controller.AuthenticationController;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;

@WebMvcTest(controllers = AuthenticationController.class, excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
})
public class AuthControllerTest {

        /*
        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private JwtUtil jwtUtil;

        @MockitoBean
        private IAuthenticationService authenticationService;

        @Autowired
        private ObjectMapper objectMapper;

        private LoginRequestDTO createLoginRequest(String email, String password) {
                return new LoginRequestDTO(email, password);
        }

        private AuthenticationResponseDTO createAuthResponse(Long id, String name, String role, String jwt,
                        String refresh) {
                AuthenticationResponseDTO response = new AuthenticationResponseDTO(id, name, role);
                response.setJwtToken(jwt);
                response.setRefreshToken(refresh);
                return response;
        }
        /*
        @Test
        @DisplayName("Login cliente exitoso devuelve respuesta con datos y status 200")
        void testLoginClientExitoso() throws Exception {
                // Arrange
                var request = createLoginRequest("cliente@correo.com", "password123");
                var response = createAuthResponse(1L, "Juan Pérez", "CLIENT", "fake-jwt-token", "fake-refresh-token");

                when(authenticationService.authenticateClient(any(LoginRequestDTO.class)))
                                .thenReturn(response);

                // Act & Assert
                mockMvc.perform(post("/api/auth/login/client")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.usuarioId").value(1))
                                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                                .andExpect(jsonPath("$.role").value("CLIENT"));
        }

        @Test
        @DisplayName("Login empleado exitoso devuelve respuesta con datos y status 200")
        void testLoginEmployeeExitoso() throws Exception {
                // Arrange
                var request = createLoginRequest("empleado@correo.com", "adminpass");
                var response = createAuthResponse(2L, "Ana Sánchez", "EMPLOYEE", "fake-jwt-token-empleado",
                                "fake-refresh-token-empleado");

                when(authenticationService.authenticateEmployee(any(LoginRequestDTO.class)))
                                .thenReturn(response);

                // Act & Assert
                mockMvc.perform(post("/api/auth/login/employee")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.usuarioId").value(2))
                                .andExpect(jsonPath("$.name").value("Ana Sánchez"))
                                .andExpect(jsonPath("$.role").value("EMPLOYEE"));
        }*/
}
