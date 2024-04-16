package com.example.fluxtest;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class EventNotify {

    private List<String> events = new ArrayList<>();

    private boolean change= false;

    public void add(String data){
        events.add(data);
        this.change= true;
    }

    public void changFlag(){
        this.change = !this.change;
    }
}
