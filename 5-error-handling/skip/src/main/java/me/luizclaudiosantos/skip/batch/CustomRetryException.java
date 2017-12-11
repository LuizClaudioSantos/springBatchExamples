package me.luizclaudiosantos.skip.batch;

public class CustomRetryException extends Exception {

    public CustomRetryException(){
        super();
    }

    public CustomRetryException(String msg){
        super(msg);
    }
}
