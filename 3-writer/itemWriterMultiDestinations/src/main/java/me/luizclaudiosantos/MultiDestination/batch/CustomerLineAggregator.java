package me.luizclaudiosantos.MultiDestination.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.luizclaudiosantos.MultiDestination.model.Customer;
import org.springframework.batch.item.file.transform.LineAggregator;

public class CustomerLineAggregator implements LineAggregator<Customer> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String aggregate(Customer item) {

        try {
            return objectMapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize Customer", e);
        }
    }
}
