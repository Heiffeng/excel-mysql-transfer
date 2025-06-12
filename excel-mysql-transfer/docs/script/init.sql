CREATE TABLE `import_task` (
                               `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
                               `name` varchar(255) NOT NULL COMMENT '任务名',
                               `table_name` varchar(255) NOT NULL COMMENT '表名',
                               `table_info` json DEFAULT NULL COMMENT '表信息，包含字段映射',
                               `creator` varchar(255) DEFAULT NULL COMMENT '创建者',
                               `status` tinyint default 1 COMMENT '状态，1.正常 2.禁用',
                               `ctime` datetime DEFAULT NULL COMMENT '创建时间',
                               `utime` datetime DEFAULT NULL COMMENT '更新时间',
                               PRIMARY KEY (`id`)
);

CREATE TABLE `import_log` (
                              `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
                              `task_id` int NOT NULL COMMENT '任务ID',
                              `count` int NOT NULL COMMENT '导入数据总数',
                              `file_name` varchar(255) DEFAULT NULL COMMENT '导入文件名称',
                              `creator` varchar(255) DEFAULT NULL COMMENT '操作人',
                              `ctime` datetime NOT NULL COMMENT '导入时间',
                              PRIMARY KEY (`id`)
);


