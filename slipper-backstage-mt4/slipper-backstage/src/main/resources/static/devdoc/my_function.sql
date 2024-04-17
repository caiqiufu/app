CREATE DEFINER=`root`@`localhost` FUNCTION `my_function`(sequenceName VARCHAR(50),batchNo BIGINT(20)) RETURNS bigint
    DETERMINISTIC
BEGIN
     declare currentValue BIGINT(20);
     UPDATE unieap_seq SET val = (val+batchNo) WHERE seq_name = sequenceName;
     SET currentValue = (SELECT VAL FROM unieap_seq WHERE seq_name= sequenceName);
RETURN currentValue;
END