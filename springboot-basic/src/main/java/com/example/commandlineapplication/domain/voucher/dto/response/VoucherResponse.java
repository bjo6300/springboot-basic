package com.example.commandlineapplication.domain.voucher.dto.response;

import com.example.commandlineapplication.domain.voucher.VoucherType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class VoucherResponse {

  private final UUID voucherId;
  private final VoucherType voucherType;
  private final long discountAmount;
}
