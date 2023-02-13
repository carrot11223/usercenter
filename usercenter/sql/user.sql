use usercenter;

create table usercenter.user
(
    id           bigint auto_increment comment 'id'
        primary key,
    username     varchar(255)                       null comment '用户昵称',
    userAccount  varchar(255)                       null comment '账户',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别
',
    userPassword varchar(512)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户状态 0为正常
',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    role         int      default 0                 not null comment '用户角色： 0为普通用户，1为管理员',
    planetCode   varchar(512)                       null comment '太阳系种族人的编号'
)
    comment '用户表';

