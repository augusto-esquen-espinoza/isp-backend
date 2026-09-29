package com.isp.customers.service;

import com.isp.customers.dto.BirthRate;
import com.isp.customers.dto.IndicatorResponse;
import com.isp.customers.dto.MonthYearCount;
import com.isp.customers.repository.CustomerRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class IndicatorServiceImpl implements IndicatorService {

    private static final String[] MONTH_NAMES = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    private final CustomerRepository repository;

    public IndicatorServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public IndicatorResponse getIndicators() {
        List<MonthYearCount> birthsByMonthYear = repository.countBirthsByMonthYear();
        long totalCustomers = birthsByMonthYear.stream().mapToLong(MonthYearCount::count).sum();

        MonthYearCount peak =
                birthsByMonthYear.stream()
                        .max(Comparator.comparingLong(MonthYearCount::count))
                        .orElse(null);

        MonthYearCount lowest =
                birthsByMonthYear.stream()
                        .min(Comparator.comparingLong(MonthYearCount::count))
                        .orElse(null);

        return new IndicatorResponse(
                birthsByMonthYear,
                peak,
                lowest,
                buildBirthRates(birthsByMonthYear, totalCustomers),
                totalCustomers);
    }

    /**
     * Aggregates births by calendar month (across all years) and computes the
     * birth rate as a percentage of the total number of customers.
     */
    private List<BirthRate> buildBirthRates(List<MonthYearCount> birthsByMonthYear, long total) {
        long[] perMonth = new long[12];
        for (MonthYearCount entry : birthsByMonthYear) {
            int month = entry.month();
            if (month >= 1 && month <= 12) {
                perMonth[month - 1] += entry.count();
            }
        }

        List<BirthRate> rates = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            double rate =
                    total == 0
                            ? 0.0
                            : BigDecimal.valueOf(perMonth[i] * 100.0 / total)
                                    .setScale(2, RoundingMode.HALF_UP)
                                    .doubleValue();
            rates.add(new BirthRate(i + 1, MONTH_NAMES[i], perMonth[i], rate));
        }
        return rates;
    }
}
