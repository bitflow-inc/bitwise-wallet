CREATE TABLE `tb_crypto_master` (
  `upd_dt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last Update Timestamp',
  `reg_dt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'First Creation Timestamp',
  `symbol` VARCHAR(10) NOT NULL COMMENT 'Cryptocurrency Symbol',
  `best_height` BIGINT NOT NULL DEFAULT 0 COMMENT 'Best Block Height at the Blockchain',
  `synched_height` BIGINT NOT NULL DEFAULT 0 COMMENT 'Last Synched Block Height at this Application',
  `decimals` INTEGER NOT NULL DEFAULT 8 COMMENT 'Cryptocurrency Decimal Length',
  `send_mast_addr` VARCHAR(128) COMMENT 'Send Master Address if use',
  `last_tx_fee` DECIMAL(10,8) DEFAULT 0 COMMENT 'Latest Transaction Fee',
  `last_gas_price` DECIMAL(8,4) DEFAULT 0 COMMENT 'Latest GAS price used if available',
  `last_gas_used` DECIMAL(8,4) DEFAULT 0 COMMENT 'Latest GAS amount used if available',
  PRIMARY KEY (`symbol`)
);

CREATE TABLE `tb_address` (
  `reg_dt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'First Creation Timestamp',
  `symbol` VARCHAR(10) NOT NULL COMMENT 'Cryptocurrency Symbol',
  `uid` INTEGER NOT NULL COMMENT 'Wallet User ID',
  `addr` VARCHAR(128) NOT NULL COMMENT 'Wallet Address',
  `balance` DECIMAL(36,8) NOT NULL DEFAULT 0 COMMENT 'Cryptocurrency Balance',
  `account` VARCHAR(100) COMMENT 'Wallet Account if use',
  `tag` VARCHAR(100) COMMENT 'Address Tag if use',
  `broker_id` CHAR(4) COMMENT 'User\'s Kafka Broker Id if use',
  PRIMARY KEY (`symbol`,`addr`)
);

CREATE TABLE `tb_trans` (
  `reg_dt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'First Creation Timestamp',
  `upd_dt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last Update Timestamp',
  `re_notify` CHAR(1) NOT NULL DEFAULT 'N' COMMENT 'Notify Again if not Delievered',
  `symbol` VARCHAR(10) NOT NULL COMMENT 'Cryptocurrency Symbol',
  `txid` VARCHAR(255) NOT NULL COMMENT 'Transaction ID',
  `tx_idx` VARCHAR(10) COMMENT 'Transaction Index if use',
  `confirm` INT NOT NULL DEFAULT 0 COMMENT 'Confirmation Count',
  `block_id` VARCHAR(128) COMMENT 'Block ID where TX is located at',
  `order_id` VARCHAR(20) COMMENT 'Send Request unique Id',
  `uid` INTEGER NOT NULL COMMENT 'Unique User ID',
  `from_addr` VARCHAR(128) COMMENT 'Sender Address',
  `from_account` VARCHAR(20) COMMENT 'Sender Account',
  `from_tag` VARCHAR(20) COMMENT 'Sender Tag',
  `amount` DECIMAL(36,8) NOT NULL DEFAULT 0 COMMENT 'Cryptocurrency Amount Sent',
  `notifiable` CHAR(1) NOT NULL DEFAULT 'Y' COMMENT 'Determine Notify TX Status',
  `noti_cnt` INT NOT NULL DEFAULT 0 COMMENT 'Notified Count',
  `to_addr` VARCHAR(128) NOT NULL COMMENT 'Receiver Address',
  `to_tag` VARCHAR(20) COMMENT 'Receiver Tag if use',
  `expt_fee` DECIMAL(10,8) COMMENT 'Expected Fee',
  `real_fee` DECIMAL(10,8) COMMENT 'Practical Fee Amount paid',
  `tx_time` BIGINT DEFAULT 0 COMMENT 'Unix Timestamp formatted Transaction Time',
  `err_msg` VARCHAR(200) COMMENT 'Error Message',
  `broker_id` CHAR(4) COMMENT 'User\'s Kafka Broker Id if use',
  PRIMARY KEY (`symbol`,`txid`)
);

