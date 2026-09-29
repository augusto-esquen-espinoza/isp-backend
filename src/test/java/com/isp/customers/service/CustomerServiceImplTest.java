package com.isp.customers.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.isp.customers.dto.CreateCustomerRequest;
import com.isp.customers.dto.CustomerResponse;
import com.isp.customers.entity.Customer;
import com.isp.customers.exception.DuplicateCustomerException;
import com.isp.customers.repository.CustomerRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock private CustomerRepository repository;

    @InjectMocks private CustomerServiceImpl service;

    private CreateCustomerRequest request() {
        return new CreateCustomerRequest(
                "Augusto", "Esquen", "augusto@example.com", "12345678", LocalDate.of(1995, 5, 20));
    }

    @Test
    @DisplayName("create persists a new customer and returns its representation")
    void create_whenDniAndEmailAreNew_persistsCustomer() {
        when(repository.existsByDni("12345678")).thenReturn(false);
        when(repository.existsByEmail("augusto@example.com")).thenReturn(false);
        when(repository.save(any(Customer.class)))
                .thenReturn(
                        Customer.builder()
                                .id(1L)
                                .nombre("Augusto")
                                .apellido("Esquen")
                                .email("augusto@example.com")
                                .dni("12345678")
                                .fechaCreacion(LocalDateTime.of(2026, 1, 1, 10, 0))
                                .fechaNacimiento(LocalDate.of(1995, 5, 20))
                                .build());

        CustomerResponse response = service.create(request());

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("augusto@example.com");
        assertThat(response.dni()).isEqualTo("12345678");
        assertThat(response.fechaCreacion()).isNotNull();
        verify(repository).save(any(Customer.class));
    }

    @Test
    @DisplayName("create rejects a duplicated dni")
    void create_whenDniAlreadyExists_throwsDuplicateCustomerException() {
        when(repository.existsByDni("12345678")).thenReturn(true);

        assertThatThrownBy(() -> service.create(request()))
                .isInstanceOf(DuplicateCustomerException.class)
                .hasMessageContaining("dni");

        verify(repository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("findAll without filters returns every customer")
    void findAll_withoutFilters_returnsAllCustomers() {
        when(repository.findAll(any(org.springframework.data.domain.Sort.class)))
                .thenReturn(
                        List.of(
                                Customer.builder().id(1L).dni("12345678").build(),
                                Customer.builder().id(2L).dni("87654321").build()));

        List<CustomerResponse> result = service.findAll(null, null);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("findAll with a dni filter returns the matching customer")
    void findAll_withDni_returnsMatch() {
        when(repository.findByDni("12345678"))
                .thenReturn(Optional.of(Customer.builder().id(1L).dni("12345678").build()));

        List<CustomerResponse> result = service.findAll("12345678", null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).dni()).isEqualTo("12345678");
    }
}
