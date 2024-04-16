package com.example.fluxtest;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter implements Filter {

    private EventNotify eventNotify;
    public MyFilter(EventNotify eventNotify){
        this.eventNotify=eventNotify;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("필터실행됨");

        HttpServletResponse servletResponse = (HttpServletResponse) response;

        servletResponse.setCharacterEncoding("utf-8"); // 문자 인코딩 설정
//        response.setContentType("text/plain;charset=utf-8"); // Content-Type 설정
        response.setContentType("text/event-stream;charset=utf-8"); // Content-Type 설정

        PrintWriter out = servletResponse.getWriter();

        for (int i = 0; i<5; i++){
            out.print("응답 : "+i+"\n");
            out.flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        while(true){
            try {
                if(eventNotify.isChange()){
                    int lastIndex = eventNotify.getEvents().size()-1;
                    out.print("응답 : "+eventNotify.getEvents().get(lastIndex)+"\n");
                    out.flush();
                    eventNotify.changFlag();
                }
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
