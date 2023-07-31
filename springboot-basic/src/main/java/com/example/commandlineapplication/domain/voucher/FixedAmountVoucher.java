package com.example.commandlineapplication.domain.voucher;

import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FixedAmountVoucher extends Voucher {

  private static final long MIN_DISCOUNTED_PRICE = 0L;
  private final UUID voucherId;
  private final long discount;

  @Override
  public UUID getVoucherId() {
    return voucherId;
  }

  @Override
  public VoucherType getVoucherType() {
    return VoucherType.FIXED;
  }

  @Override
  public long getDiscount() {
    return discount;
  }

  public double discountedPrice(long price) {
    validateDiscountedPrice(price);
    if (price < discount) {
      return MIN_DISCOUNTED_PRICE;
    }
    return price - discount;
  }

  private void validateDiscountedPrice(long price) {
    if (price < MIN_DISCOUNTED_PRICE) {
      throw new IllegalArgumentException("고정 할인 범위를 확인해주세요.");
    }
  }
}
