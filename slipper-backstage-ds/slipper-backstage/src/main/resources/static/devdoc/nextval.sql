CREATE DEFINER=`root`@`localhost` FUNCTION `nextval`(sequenceName VARCHAR(50),batchNo BIGINT(20)) RETURNS bigint
    DETERMINISTIC
BEGIN
     declare currentValue BIGINT(20);
     SET SQL_SAFE_UPDATES = 0;
     IF batchNo = 0 THEN
		UPDATE unieap_seq SET val = (val+INCREMENT) WHERE seq_name = sequenceName;
     ELSE
		UPDATE unieap_seq SET val = (val+batchNo) WHERE seq_name = sequenceName;
	 END IF;
     SET currentValue = (SELECT VAL FROM unieap_seq WHERE seq_name= sequenceName);
RETURN currentValue;  
END