/*
MySQL Data Transfer
Source Host: localhost
Source Database: 127.0.0.1
Target Host: localhost
Target Database: 127.0.0.1
Date: 2010/5/30 �U�� 10:53:59
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for tcptable
-- ----------------------------
CREATE TABLE `tcptable` (
  `localaddr` varchar(15) NOT NULL,
  `localport` int(5) NOT NULL,
  `remaddr` varchar(15) NOT NULL,
  `remport` int(5) NOT NULL,
  `status` varchar(15) NOT NULL,
  `start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `map` text NOT NULL,
  `isboot` int(1) NOT NULL,
  PRIMARY KEY (`localaddr`,`localport`,`remaddr`,`remport`,`start`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
