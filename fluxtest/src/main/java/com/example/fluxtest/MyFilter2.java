package com.example.fluxtest;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter2 implements Filter {

    private EventNotify eventNotify;
    public MyFilter2(EventNotify eventNotify){
        this.eventNotify=eventNotify;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("필터실행됨");
        
        //데이터를 하나 발생 시켜서 끼워넣기
        eventNotify.add("새로운 데이터");

    }
}
