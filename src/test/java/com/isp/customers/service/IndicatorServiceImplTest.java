package com.isp.customers.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.isp.customers.dto.IndicatorResponse;
import com.isp.customers.dto.MonthYearCount;
import com.isp.customers.repository.CustomerRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IndicatorServiceImplTest {

    @Mock private CustomerRepository repository;

    @InjectMocks private IndicatorServiceImpl service;

    @Test
    @DisplayName("getIndicators computes totals, peak, lowest and monthly birth rates")
    void getIndicators_computesIndicators() {
        List<MonthYearCount> data =
                List.of(
                        new MonthYearCount(1990, 1, 10L),
                        new MonthYearCount(1991, 2, 2L),
                        new MonthYearCount(1992, 1, 8L));
        when(repository.countBirthsByMonthYear()).thenReturn(data);

        IndicatorResponse result = service.getIndicators();

        assertThat(result.totalCustomers()).isEqualTo(20L);
        assertThat(result.birthsByMonthYear()).hasSize(3);
        assertThat(result.peakMonthYear()).isEqualTo(new MonthYearCount(1990, 1, 10L));
        assertThat(result.lowestMonthYear()).isEqualTo(new MonthYearCount(1991, 2, 2L));

        assertThat(result.birthRateByMonth()).hasSize(12);
        assertThat(result.birthRateByMonth().get(0).month()).isEqualTo(1);
        assertThat(result.birthRateByMonth().get(0).count()).isEqualTo(18L);
        assertThat(result.birthRateByMonth().get(0).rate()).isEqualTo(90.00);
        assertThat(result.birthRateByMonth().get(1).rate()).isEqualTo(10.00);
        assertThat(result.birthRateByMonth().get(2).rate()).isEqualTo(0.00);
    }

    @Test
    @DisplayName("getIndicators returns zeroed indicators when there are no customers")
    void getIndicators_withoutCustomers_returnsZeroes() {
        when(repository.countBirthsByMonthYear()).thenReturn(List.of());

        IndicatorResponse result = service.getIndicators();

        assertThat(result.totalCustomers()).isZero();
        assertThat(result.birthsByMonthYear()).isEmpty();
        assertThat(result.peakMonthYear()).isNull();
        assertThat(result.lowestMonthYear()).isNull();
        assertThat(result.birthRateByMonth()).hasSize(12);
        assertThat(result.birthRateByMonth()).allSatisfy(rate -> assertThat(rate.rate()).isZero());
    }
}
