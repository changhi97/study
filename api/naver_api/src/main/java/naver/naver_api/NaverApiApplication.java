package naver.naver_api;

import naver.naver_api.goolevision.DetectText;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class NaverApiApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(NaverApiApplication.class, args);
	}
}
