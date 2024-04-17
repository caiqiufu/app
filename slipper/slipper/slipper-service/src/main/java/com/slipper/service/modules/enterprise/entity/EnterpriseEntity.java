package com.slipper.service.modules.enterprise.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.slipper.common.validator.group.Create;
import com.slipper.common.validator.group.Update;

import lombok.Data;

/**
 * 企业
 *
 * @author gumingchen
 * @email 1240235512@qq.com
 * @date 1995-08-30 00:00:00
 */
@Data
@TableName("enterprise")
public class EnterpriseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空", groups = Update.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {Create.class, Update.class})
    private String name;
    /**
     * Logo
     */
    private String logo;
    /**
     * Logo
     */
    @TableField("`describe`")
    private String describe;
    /**
     * 状态：0-禁用 1-正常
     */
    private Integer status;
    /**
     * 创建者
     */
    private Long creator;
    /**
     * 创建时间
     */
    @JsonProperty("created_at")
    private Date createdAt;
    /**
     * 更新者
     */
    private Long updater;
    /**
     * 更新时间
     */
    @JsonProperty("updated_at")
    private Date updatedAt;
    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer deleted;
}
