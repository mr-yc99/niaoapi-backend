package com.foryaapicommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户接口调用次数表
 * @TableName user_api_info
 */
@TableName(value ="user_api_info")
@Data
public class UserApiInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long apiId;

    /**
     * 总共调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0 允许调用 1 不允许调用
     */
    private Integer status;

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