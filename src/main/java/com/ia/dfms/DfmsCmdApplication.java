package com.ia.dfms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
  // @EnableBinding(value= {DfmsChannel.class})
public class DfmsCmdApplication {

    public static void main(String[] args) {
        SpringApplication.run(DfmsCmdApplication.class, args);
    }
}
