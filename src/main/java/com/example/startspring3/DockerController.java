package com.example.startspring3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DockerController {

    @GetMapping("/")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello12");
    }

    @GetMapping("/2")
    public ResponseEntity<String> hello2(){
        return ResponseEntity.ok("Hello1");
    }
}
