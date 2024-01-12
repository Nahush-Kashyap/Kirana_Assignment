package learning.kirana_stores.services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@EnableCaching
public class CurrencyConvertionService {

    // API URL for fetching the latest exchange rates
    private final String apiUrl = "https://api.fxratesapi.com/latest";

    /**
     * Retrieves the conversion rate from the source currency to the target currency.
     * Caches the result using Spring's caching mechanism.
     *
     * @param sourceCurrency The source currency code.
     * @param targetCurrency The target currency code.
     * @return The conversion rate as a BigDecimal.
     */


    @Cacheable(cacheManager = "cacheManager", cacheNames = "conversionRatesCache")
    public BigDecimal getConversionRate(String sourceCurrency, String targetCurrency) {
        String url = apiUrl.replace("{base}", sourceCurrency);
        RestTemplate restTemplate = new RestTemplate();
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("rates")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("rates");

                if (rates.containsKey(sourceCurrency) && rates.containsKey(targetCurrency)) {
                    BigDecimal sourceRate = convertToBigDecimal(rates.get(sourceCurrency));
                    BigDecimal targetRate = convertToBigDecimal(rates.get(targetCurrency));

                    // Calculate the conversion rate by dividing target rate by source rate
                    return targetRate.divide(sourceRate, 4, RoundingMode.HALF_UP);
                }
            }
            throw new RuntimeException("Unable to fetch conversion rate");
        } catch (Exception e) {
            throw new RuntimeException("Error fetching conversion rates from API");
        }
    }

    /**
     * Converts an Object value to BigDecimal.
     *
     * @param value The value to be converted.
     * @return The value as BigDecimal.
     */
    private BigDecimal convertToBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
    }
}
