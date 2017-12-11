package me.luizclaudiosantos.compositeItemProcessors.batch;


import me.luizclaudiosantos.compositeItemProcessors.model.*;
import org.springframework.batch.item.ItemProcessor;

public class FilteringItemProcessor implements ItemProcessor<Customer, Customer>{
    @Override
    public Customer process(Customer item) throws Exception {
        if(item.getId() % 2 == 0 ){
            return null;
        }else {
            return item;
        }
    }
}
