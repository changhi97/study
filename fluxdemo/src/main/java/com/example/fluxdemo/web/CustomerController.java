package com.example.fluxdemo.web;

import com.example.fluxdemo.domain.Customer;
import com.example.fluxdemo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
//@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final Sinks.Many<Customer> sink;

    //A요청 -> Flux
    //B요청 -> Flux
    //요청에 따른 스트림이 있고 다 다르다
    //두개의 스트름을 합칠수있다.
    //Flux.merge으로 스트림을 합치면 sink가 맞춰진다.
    //sink객체는 모든 클라이언트들이 접근할수있다.(모든 웹플럭스 요청을 sink할 수 있다)

    public CustomerController(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;

        //멀티캐스트 - 새로 푸시된 데이터만 전송해주는 방식
        sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping("/flux")
    public Flux<Integer> flux(){
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> fluxstream(){
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/customerFlux",  produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> findAllFlux(){
        return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping("/customer")
    public Flux<Customer> findAll(){
        //find 할때 onNext가 실행되는건지 아니면 return실행될때 되는건지? -> findAll할때 onNext한다.

        //데이터베이스의 데이터에 onSubscribe한다. -> next하다가 onComplete하는순간 응답이 된다.
        return customerRepository.findAll().log();
    }



    @GetMapping("/customer/{member_id}")
    public Mono<Customer> findById(@PathVariable("member_id") Long id){
        return customerRepository.findById(id).log();
    }

    //MediaType.APPLICATION_STEAM_JSON_VALUE 데이터를 전송하면 끝이 나버린다
    //반면, sse는 전송이 완료되어도 연결이 유지된다.
    //일단 이렇게 하면 끊긴다
    @GetMapping(value = "/customer/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> findAllSSE(){
        return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
    }


//   Sink의 역할❗
//   두 개 이상의 Stream의 Sink를 맞춰주는 역할이다.
//   조금 더 쉽게 설명하면,
//   Client A와 Client B에서 각자 요청을 보내면 서버는 Flux A와 Flux B를 클라이언트에 맞게 응답한다.
//            즉, 서로 다른 Stream이 생긴다.
//   이때, Sink는 서로 다른 Stream을 연결하는 역할이다.
//   Event(Data)가 발생되면 Sink로 연결된 각 Stream으로 Event를 수신할 수 있다.

    @GetMapping(value = "/customer/sse2")   //produces = MediaType.TEXT_EVENT_STREAM_VALUE생략가능
    public Flux<ServerSentEvent<Customer>> findAllSSE2(){
        //싱크된 플럭스데이터가 있는지확인
        //ServerSentEvent로 return되면 produces가 MediaType.TEXT_EVENT_STREAM_VALUE가 되어서 생략가능하다
        
        //sink.asFlux().blockLast();가 없으면 연결이 끊기면 다시 연결안댐.
        //sink.asFlux().blockLast();가 있어야 재 연결가능

        //sink.asFlux().blockLast()는 Flux가 마지막 요소를 발행할 때까지 기다리는 데 사용
        // 클라이언트의 연결이 끊길 경우에 다시 연결할 수 있도록 Sink를 닫고 마지막 요소가 발행될 때까지 대기
        return sink.asFlux().map(c-> ServerSentEvent.builder(c).build()).doOnCancel(()->{
            sink.asFlux().blockLast();
        });
    }

    @PostMapping("/cumtomer")
    public Mono<Customer> save(){
        //이렇게 하면 sink로 푸시하면 화면에 뜬다. 다만 새로고침하면 연결이 끊기고 다 안댐]
        return customerRepository.save(new Customer("kim","changhee")).doOnNext(c->{
            sink.tryEmitNext(c);
        });
    }
}
