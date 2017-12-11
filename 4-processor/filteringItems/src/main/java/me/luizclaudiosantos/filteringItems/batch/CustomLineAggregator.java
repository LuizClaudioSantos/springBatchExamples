package me.luizclaudiosantos.filteringItems.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.luizclaudiosantos.filteringItems.model.Customer;
import org.springframework.batch.item.file.transform.LineAggregator;

public class CustomLineAggregator implements LineAggregator<Customer> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String aggregate(Customer item) {
        try {
            return objectMapper.writeValueAsString(item);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize Customer", e);
        }
    }

}
