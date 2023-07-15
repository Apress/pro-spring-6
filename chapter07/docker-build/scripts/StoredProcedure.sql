-- Change the delimiter
DELIMITER //
-- Create the stored procedure
CREATE OR REPLACE PROCEDURE getFirstNameByIdProc(IN in_id INT, OUT fn_res VARCHAR(60))
BEGIN
    SELECT first_name INTO fn_res FROM SINGER WHERE id = in_id;
END
//
-- Change back the delimiter
DELIMITER ;