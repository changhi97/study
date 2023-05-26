package naver.naver_api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
@Slf4j
public class kakaoMapController {

    @GetMapping("/show")
    public String showMap(){
        log.info("loading kakao map");
        return "map/kakaoMap";
    }
}
