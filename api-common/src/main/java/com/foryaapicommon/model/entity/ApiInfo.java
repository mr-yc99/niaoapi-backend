package com.foryaapicommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * api信息表
 * @TableName api_info
 */
@TableName(value ="api_info")
@Data
public class ApiInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * api名
     */
    private String apiName;

    /**
     * 描述
     */
    private String description;

    /**
     * api地址
     */
    private String url;


    /**
     * 接口路径
     */
    private String path;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;


    /**
     * 请求参数
     * {["name": "username", "type": "string"]}
     */
    private String requestParams;

    /**
     * 0 关闭 1 开启
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 创建人id
     */
    private Long userId;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Integer is_deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}