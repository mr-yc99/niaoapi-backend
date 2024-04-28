package com.foryaapi.model.dto.apiinfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户调用测试接口请求
 *
 * @TableName product
 */
@Data
public class ApiInfoInvokeRequest implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 用户请求参数
     */
    private String userRequestParams;

    //剩下的参数都可以通过接口ID查出来
    /**
     * 接口路径
     */
    private String path;
    /**
     * 接口地址
     */
    private String url;
    /**
     * 请求类型
     */
    private String method;


    private static final long serialVersionUID = 1L;
}