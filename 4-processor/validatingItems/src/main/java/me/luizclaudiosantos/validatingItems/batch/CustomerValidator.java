package me.luizclaudiosantos.validatingItems.batch;

import me.luizclaudiosantos.validatingItems.model.Customer;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;


public class CustomerValidator implements Validator<Customer> {


    @Override
    public void validate(Customer value) throws ValidationException {
        if(value.getFirstName().contains("1")){
           throw  new ValidationException("The customer name contains 1");
        }
    }
}
