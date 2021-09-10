create table job_config
(
    id          int auto_increment comment 'ID' primary key,
    job_name    varchar(60)   not null comment '任务名称',
    job_name_cn varchar(60) null comment '任务名称(中文)',
    job_group   varchar(60)   not null comment '任务分组',
    job_cron    varchar(20)   not null comment '任务cron表达式',
    job_status  int default 0 not null comment '任务状态，0停用，1启用',
    job_groovy  text          not null comment '任务groovy脚本',
    job_desc    varchar(60) null comment '任务描述',
    update_time datetime null comment '最近一次更新时间',
    create_time datetime null comment '创建时间'
) comment '任务配置';

create table job_execute_log
(
    id          bigint auto_increment comment 'ID' primary key,
    job_id      int not null comment '任务ID',
    job_name    varchar(30) null comment '任务名称',
    job_group   varchar(30) null comment '任务分组',
    host_ip     varchar(30) null comment '执行机器ip',
    status      int null comment '执行状态，0成功，1失败',
    expend_time int null comment '消耗时间，单位ms',
    err_msg     varchar(100) null comment '错误描述',
    create_time datetime null comment '记录创建时间'
) comment '任务执行日志';