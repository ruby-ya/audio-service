/*
 Navicat Premium Data Transfer

 Source Server         : [MySQL5.7]127.0.0.1_root_3306
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 127.0.0.1:3306
 Source Schema         : jdbc_demo

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 08/10/2020 13:55:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for audio_file
-- ----------------------------
DROP TABLE IF EXISTS `audio_file`;
CREATE TABLE `audio_file`  (
                               `id` int(11) NOT NULL AUTO_INCREMENT,
                               `url` varchar(255)  NULL DEFAULT NULL,
                               `file_name` varchar(255)  NULL DEFAULT NULL,
                               `file_path` varchar(255)  NULL DEFAULT NULL,
                               `update_time` datetime(0) NULL DEFAULT NULL,
                               `create_time` datetime(0) NULL DEFAULT NULL,
                               `remark` varchar(255)  NULL DEFAULT NULL,
                               `isdeleted` tinyint(1) NULL,
                               PRIMARY KEY (`id`)
);

SET FOREIGN_KEY_CHECKS = 1;
