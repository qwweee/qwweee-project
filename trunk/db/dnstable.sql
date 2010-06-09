/*
MySQL Data Transfer
Source Host: localhost
Source Database: project
Target Host: localhost
Target Database: project
Date: 2010/5/28 ¤U¤È 05:05:02
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for dnstable
-- ----------------------------
CREATE TABLE `dnstable` (
  `no` int(10) NOT NULL AUTO_INCREMENT,
  `ip` text NOT NULL,
  `dns` text NOT NULL,
  `status` int(2) NOT NULL,
  `time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
