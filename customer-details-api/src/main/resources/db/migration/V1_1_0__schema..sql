CREATE TABLE IF NOT EXISTS `customer` (
  `customer_id` varchar(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address_line_one` varchar(150) NOT NULL,
  `address_line_two` varchar(150),
  `town` varchar(60) NOT NULL,
  `county` varchar(50),
  `country` varchar(60) NOT NULL,
  `post_code` varchar(50) NOT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;