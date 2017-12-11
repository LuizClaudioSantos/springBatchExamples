package me.luizclaudiosantos.skip.batch;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class SkipItemWriter implements ItemWriter<String> {

    private boolean skip = false;
    private int attemptCount = 0;


    @Override
    public void write(List<? extends String> items) throws Exception {
        for(String item : items){
            System.out.println("writing item " + item);
            if( skip && item.equalsIgnoreCase("-84")){
                attemptCount++;
                throw  new CustomRetryException("Write failed.  Attempt:" + attemptCount);
            } else {
                System.out.println(item);
            }
        }
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }
}
