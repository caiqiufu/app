DROP EVENT IF EXISTS clear_jr_source_data;
DELIMITER //
CREATE EVENT clear_jr_source_data
ON SCHEDULE
    EVERY 1 WEEK
    STARTS TIMESTAMP(CURRENT_DATE + INTERVAL (5 - DAYOFWEEK(CURRENT_DATE)) + 6 DAY + '06:30:00')
DO
BEGIN
  -- Your multiple SQL statements here
  SET SQL_SAFE_UPDATES = 0;
  -- insert into `unieap-ea`.test values(CURRENT_TIME,'test');
  insert into trade_price_history select * from trade_price;
  delete from trade_price;
  insert into trade_daily_detail_history select * from trade_daily_detail;
  delete from trade_daily_detail;
  insert into trade_daily_sum_history select * from trade_daily_sum;
  delete from trade_daily_sum;
  insert into trade_sum_history select * from trade_sum;
  delete from trade_sum;
  insert into trade_latest_history select * from trade_latest;
  delete from trade_latest;
END;
//
DELIMITER ;
SET GLOBAL event_scheduler = ON;