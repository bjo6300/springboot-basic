package com.example.commandlineapplication.global.io;

import org.springframework.stereotype.Component;

@Component
public interface Input {

  String input();

  Integer getDiscount();
}
