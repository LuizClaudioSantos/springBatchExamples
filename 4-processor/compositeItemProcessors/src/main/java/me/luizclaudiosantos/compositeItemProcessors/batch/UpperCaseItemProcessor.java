package me.luizclaudiosantos.compositeItemProcessors.batch;

import me.luizclaudiosantos.compositeItemProcessors.model.Customer;
import org.springframework.batch.item.ItemProcessor;

public class UpperCaseItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer item) throws Exception {
        return new Customer(item.getId(),
                item.getFirstName().toUpperCase(),
                item.getLasName().toUpperCase(),
                item.getBirthday());
    }
}
