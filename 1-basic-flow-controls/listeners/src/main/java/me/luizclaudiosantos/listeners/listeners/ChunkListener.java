package me.luizclaudiosantos.listeners.listeners;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

public class ChunkListener {

    @BeforeChunk
    public void beforeChunck(ChunkContext context){
        System.out.println(">> Before the chunk");
    }

    @AfterChunk
    public void afterChunck(ChunkContext context){
        System.out.println("<< After the chunk");
    }
}
