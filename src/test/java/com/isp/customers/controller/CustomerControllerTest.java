package com.isp.customers.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.isp.customers.dto.CreateCustomerRequest;
import com.isp.customers.dto.CustomerResponse;
import com.isp.customers.service.CustomerService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private CustomerService customerService;

    private static final String VALID_BODY =
            """
            {
              "nombre": "Augusto",
              "apellido": "Esquen",
              "email": "augusto@example.com",
              "dni": "12345678",
              "fechaNacimiento": "1995-05-20"
            }
            """;

    @Test
    @DisplayName("POST /api/customers returns 201 for a valid payload")
    void create_withValidPayload_returnsCreated() throws Exception {
        when(customerService.create(any(CreateCustomerRequest.class)))
                .thenReturn(
                        new CustomerResponse(
                                1L,
                                "Augusto",
                                "Esquen",
                                "augusto@example.com",
                                "12345678",
                                LocalDateTime.of(2026, 1, 1, 10, 0),
                                LocalDate.of(1995, 5, 20)));

        mockMvc.perform(
                        post("/api/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("augusto@example.com"));
    }

    @Test
    @DisplayName("POST /api/customers returns 400 for an invalid payload")
    void create_withInvalidPayload_returnsBadRequest() throws Exception {
        String invalidBody =
                """
                {
                  "nombre": "",
                  "apellido": "",
                  "email": "not-an-email",
                  "dni": "12",
                  "fechaNacimiento": null
                }
                """;

        mockMvc.perform(
                        post("/api/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidBody))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);
    }

    @Test
    @DisplayName("GET /api/customers returns the customer list")
    void findAll_returnsCustomerList() throws Exception {
        when(customerService.findAll(null, null))
                .thenReturn(
                        List.of(
                                new CustomerResponse(
                                        1L,
                                        "Augusto",
                                        "Esquen",
                                        "augusto@example.com",
                                        "12345678",
                                        LocalDateTime.of(2026, 1, 1, 10, 0),
                                        LocalDate.of(1995, 5, 20))));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].dni").value("12345678"));
    }
}
