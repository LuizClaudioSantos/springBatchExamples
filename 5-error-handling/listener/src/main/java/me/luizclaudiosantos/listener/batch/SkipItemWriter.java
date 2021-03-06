package me.luizclaudiosantos.listener.batch;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class SkipItemWriter implements ItemWriter<String> {



    @Override
    public void write(List<? extends String> items) throws Exception {
        for(String item : items){
            if(  item.equalsIgnoreCase("-84")){
                throw  new CustomException("Write failed.  Attempt:" + item);
            } else {
                System.out.println(item);
            }
        }
    }


}
