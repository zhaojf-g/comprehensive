package com.zhaojf.pingan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName appointment_record
 */
@TableName(value ="appointment_record")
@Data
public class AppointmentRecord implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String telephone;

    /**
     * 
     */
    private String vehicleNo;

    /**
     * 
     */
    private Date date;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}