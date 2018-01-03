package com.flockinger.spongeblogSP;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpongeblogSpApplication { 

  @RequestMapping(value = "/")
  public void index(HttpServletResponse response) throws IOException {
    response.sendRedirect("/swagger-ui.html");
  }

  public static void main(String[] args) {
    SpringApplication.run(SpongeblogSpApplication.class, args);
  }
}
