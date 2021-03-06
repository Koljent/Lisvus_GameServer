-- ----------------------------
-- Table structure for `custom_merchant_buylists`
-- ----------------------------
DROP TABLE IF EXISTS `custom_merchant_buylists`;
CREATE TABLE `custom_merchant_buylists` (
  `item_id` decimal(9,0) NOT NULL DEFAULT '0',
  `price` decimal(11,0) NOT NULL DEFAULT '0',
  `shop_id` decimal(9,0) NOT NULL DEFAULT '0',
  `item_order` decimal(4,0) NOT NULL DEFAULT '0',
  `count` INT( 11 ) NOT NULL DEFAULT '-1',
  `currentCount` INT( 11 ) NOT NULL DEFAULT '-1',
  time INT NOT NULL DEFAULT '0',
  savetimer DECIMAL(20,0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`shop_id`,`item_order`)
);