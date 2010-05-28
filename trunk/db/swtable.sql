/*
MySQL Data Transfer
Source Host: localhost
Source Database: 127.0.0.1
Target Host: localhost
Target Database: 127.0.0.1
Date: 2010/5/28 ¤U¤È 05:04:13
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for swtable
-- ----------------------------
CREATE TABLE `swtable` (
  `index` int(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `id` varchar(10) NOT NULL,
  `path` text NOT NULL,
  `parameters` text NOT NULL,
  `type` varchar(20) NOT NULL,
  `status` varchar(15) NOT NULL,
  `start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `end` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `map` text NOT NULL,
  PRIMARY KEY (`index`,`start`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
