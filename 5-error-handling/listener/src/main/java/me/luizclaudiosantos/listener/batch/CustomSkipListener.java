package me.luizclaudiosantos.listener.batch;

import org.springframework.batch.core.SkipListener;

public class CustomSkipListener implements SkipListener {

    @Override
    public void onSkipInRead(Throwable t) {

    }

    @Override
    public void onSkipInWrite(Object item, Throwable t) {
        System.out.println(">>Skipping " + item + " because write has failed");
    }

    @Override
    public void onSkipInProcess(Object item, Throwable t) {
        System.out.println(">>Skipping " + item + " because process has failed");
    }
}
