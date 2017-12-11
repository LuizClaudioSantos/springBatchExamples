package me.luizclaudiosantos.filteringItems.batch;


import me.luizclaudiosantos.filteringItems.model.*;
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
