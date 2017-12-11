package me.luizclaudiosantos.listener.batch;

import org.springframework.batch.item.ItemProcessor;

public class SkipItemProcessor implements ItemProcessor<String, String> {




    @Override
    public String process(String item) throws Exception {
        System.out.println("processing item " + item);
        if( item.equalsIgnoreCase("42")){

            System.out.println("Processing of item " + item + " failed");
             throw new CustomException("Process failed. Attempt: " + item);

        }else {
            return String.valueOf(Integer.valueOf(item) * -1);
        }
    }


}
