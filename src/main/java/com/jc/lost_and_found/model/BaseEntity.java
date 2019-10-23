package com.jc.lost_and_found.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zjc
 * @date 2019/9/27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseEntity<T extends Model<T>> extends Model<T> {

    @TableId(value = "id", type = IdType.AUTO)
    protected Long id;

    @JsonIgnore
    @TableLogic //逻辑删除标签
    private Boolean deleteFlag;//0-exist, 1-delete
}
