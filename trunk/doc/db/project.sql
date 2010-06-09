/*
MySQL Data Transfer
Source Host: localhost
Source Database: project
Target Host: localhost
Target Database: project
Date: 2010/5/13 ¤U¤È 04:51:39
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for connection_test_table
-- ----------------------------
CREATE TABLE `connection_test_table` (
  `a` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
-- Table structure for iptable
-- ----------------------------
CREATE TABLE `iptable` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ip` varchar(15) NOT NULL,
  `update` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
