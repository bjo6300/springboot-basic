package com.example.commandlineapplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommandLineApplication implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(CommandLineApplication.class);
  private final ConsoleRunner consoleRunner;

  public CommandLineApplication(ConsoleRunner consoleRunner) {
    this.consoleRunner = consoleRunner;
  }

  public static void main(String[] args) {
    LOG.info("voucher가 실행되었습니다.");
    SpringApplication.run(CommandLineApplication.class, args);
  }

  @Override
  public void run(String... args) {
    consoleRunner.run();
  }
}
