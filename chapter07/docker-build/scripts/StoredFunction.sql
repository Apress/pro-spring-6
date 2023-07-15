-- Change the delimiter
DELIMITER //
-- Create the stored procedure
CREATE FUNCTION IF NOT EXISTS getFirstNameById(in_id INT) RETURNS VARCHAR(60)
    LANGUAGE SQL
BEGIN
    RETURN (SELECT first_name FROM SINGER WHERE id = in_id);
END
//
-- Change back the delimiter
DELIMITER ;