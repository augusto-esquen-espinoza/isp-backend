package com.isp.customers.repository;

import com.isp.customers.dto.MonthYearCount;
import com.isp.customers.entity.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Data access layer for {@link Customer}.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByDni(String dni);

    Optional<Customer> findByEmail(String email);

    List<Customer> findByDniOrEmail(String dni, String email);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);

    /**
     * Groups customers by the year and month of their birth date.
     *
     * @return one entry per distinct year/month with its customer count
     */
    @Query(
            """
            SELECT new com.isp.customers.dto.MonthYearCount(
                year(c.fechaNacimiento), month(c.fechaNacimiento), count(c))
            FROM Customer c
            GROUP BY year(c.fechaNacimiento), month(c.fechaNacimiento)
            ORDER BY year(c.fechaNacimiento), month(c.fechaNacimiento)
            """)
    List<MonthYearCount> countBirthsByMonthYear();
}
