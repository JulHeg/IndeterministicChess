package test.test.IndeterministicChess.IO;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class mySpring {

    public static void main(String[] args) {
        SpringApplication.run(mySpring.class, args);
    }
    
    @PostConstruct
    public static void initialize(){
		// TODO Make something
    }
}

