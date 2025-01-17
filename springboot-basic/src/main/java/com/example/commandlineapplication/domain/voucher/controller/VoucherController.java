package com.example.commandlineapplication.domain.voucher.controller;

import com.example.commandlineapplication.domain.voucher.model.VoucherType;
import com.example.commandlineapplication.domain.voucher.service.VoucherService;
import com.example.commandlineapplication.global.io.Command;
import com.example.commandlineapplication.global.io.Console;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VoucherController implements Runnable {

  private final Logger LOG = LoggerFactory.getLogger(VoucherController.class);
  private final Console console;
  private final VoucherService voucherService;

  @Override
  public void run() {
    boolean isRunning = true;

    while (isRunning) {
      console.printMenu();
      try {
        Command command = Command.of(console.selectOption());

        switch (command) {
          case CREATE:
            VoucherType inputVoucherType = console.selectVoucherTypeOption();
            Integer inputDiscount = console.selectDiscount();

            voucherService.createVoucher(inputVoucherType, inputDiscount);
            continue;
          case LIST:
            console.printHistory();
            continue;
          case EXIT:
            isRunning = false;
        }
      } catch (Exception e) {
        LOG.warn(e.getMessage());
        return;
      }
    }
  }
}
