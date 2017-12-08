package me.luizclaudiosantos.flatFiles.config;

import org.springframework.batch.item.*;

import java.util.List;

public class SysOutItemWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> items) throws Exception {
        System.out.println("The size of this Chunk was: " + items.size());

        items.forEach(item -> System.out.println(item));
    }
}
