CREATE TABLE tb_credit (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   credit_code UUID NOT NULL,
   credit_value DECIMAL NOT NULL,
   day_first_installment date NOT NULL,
   number_of_installments INT NOT NULL,
   status SMALLINT,
   customer_id BIGINT,
   CONSTRAINT pk_tb_credit PRIMARY KEY (id)
);

ALTER TABLE tb_credit ADD CONSTRAINT uc_tb_credit_creditcode UNIQUE (credit_code);

ALTER TABLE tb_credit ADD CONSTRAINT FK_TB_CREDIT_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES tb_customer (id);