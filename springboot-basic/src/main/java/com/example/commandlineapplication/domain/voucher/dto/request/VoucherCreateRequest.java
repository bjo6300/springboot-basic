package com.example.commandlineapplication.domain.voucher.dto.request;

import com.example.commandlineapplication.domain.voucher.model.VoucherType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class VoucherCreateRequest {

  private final VoucherType voucherType;
  private final long discountAmount;

}
