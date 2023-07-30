package com.example.commandlineapplication.domain.voucher.repository;

import com.example.commandlineapplication.domain.voucher.Voucher;
import com.example.commandlineapplication.domain.voucher.VoucherType;
import com.example.commandlineapplication.domain.voucher.dto.mapper.VoucherMapper;
import com.example.commandlineapplication.domain.voucher.dto.request.VoucherCreateRequest;
import com.example.commandlineapplication.domain.voucher.service.VoucherFactory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class VoucherJdbcRepository implements VoucherRepository {

  private static final Logger LOG = LoggerFactory.getLogger(VoucherJdbcRepository.class);
  private static final int SUCCESS_EXECUTE = 1;
  private final NamedParameterJdbcTemplate template;
  private final VoucherFactory voucherFactory;
  private final VoucherMapper voucherMapper;

  public VoucherJdbcRepository(NamedParameterJdbcTemplate template, VoucherFactory voucherFactory,
      VoucherMapper voucherMapper) {
    this.template = template;
    this.voucherFactory = voucherFactory;
    this.voucherMapper = voucherMapper;
  }

  private RowMapper<Voucher> rowMapper() {
    return (resultSet, rowMap) -> {
      UUID voucherId = UUID.fromString((resultSet.getString("voucher_id")));
      VoucherType voucherType = VoucherType.valueOf(resultSet.getString("voucher_type"));
      long discount = resultSet.getLong("voucher_discount");
      VoucherCreateRequest voucherCreateRequest = voucherMapper.toCreateRequest(voucherId,
          voucherType,
          discount);

      return voucherFactory.create(voucherCreateRequest);
    };
  }

  @Override
  public Optional<Voucher> findById(UUID voucherId) {
    String sql = "select * from voucher where voucher_id = :voucherId";

    try {
      SqlParameterSource param = new MapSqlParameterSource()
          .addValue("voucherId", voucherId.toString());

      Voucher voucher = template.queryForObject(sql, param, rowMapper());

      return Optional.ofNullable(voucher);
    } catch (EmptyResultDataAccessException e) {
      LOG.error("voucherId가 존재하지 않습니다. " + e.getMessage() + e);
      return Optional.empty();
    }
  }

  @Override
  public Voucher insert(Voucher voucher) {
    String sql = "insert into voucher values (:voucherId, :voucher_discount, :voucher_type)";

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("voucherId", voucher.getVoucherId().toString())
        .addValue("voucher_discount", voucher.getDiscount())
        .addValue("voucher_type", voucher.getVoucherType().name());

    int saved = template.update(sql, param);

    if (saved != SUCCESS_EXECUTE) {
      DataAccessException exception = new IncorrectResultSizeDataAccessException(SUCCESS_EXECUTE,
          saved);
      LOG.error(exception.getMessage(), exception);
      throw exception;
    }

    return voucher;
  }

  @Override
  public List<Voucher> findAll() {
    String sql = "select * from voucher";

    return template.query(sql, rowMapper());
  }

  @Override
  public List<Voucher> findVouchersByVoucherType(VoucherType voucherType) {
    String type = voucherType.name();
    String sql = "select * from voucher where voucher_type = :type";

    return template.query(sql, Collections.singletonMap("type", type),
        rowMapper());
  }

  @Override
  public void deleteById(UUID voucherId) {
    String sql = "delete from voucher where voucher_id = :voucherId";

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("voucherId", voucherId.toString());

    int deleted = template.update(sql, param);

    if (deleted != SUCCESS_EXECUTE) {
      DataAccessException exception = new IncorrectResultSizeDataAccessException(SUCCESS_EXECUTE,
          deleted);
      LOG.error(exception.getMessage(), exception);
      throw exception;
    }
  }
}
