package com.zhaojf.pingan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    private String contactName;

    /**
     * 
     */
    private String contactTelephone;

    /**
     * 
     */
    private String vehicleNo;

    /**
     * 
     */
    private String sessionId;

    /**
     * 
     */
    private String signature;

    /**
     * 
     */
    private String number;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}