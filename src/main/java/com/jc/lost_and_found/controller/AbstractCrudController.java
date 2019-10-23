package com.jc.lost_and_found.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.lost_and_found.component.ResponseData;
import com.jc.lost_and_found.component.ResponseDataUtil;
import com.jc.lost_and_found.constant.ResultEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AbstractCrudController。提供最基本的save,updateById,removeById,detailById,listByEntity,pageByEntity方法。
 *
 * @author  hx
 * @version 2019/7/28
 */
@Slf4j
public abstract class AbstractCrudController<S extends IService<T>, T> {

    /**
     * service服务
     */
    @Autowired
    protected S service;

     /**
     * 添加
     *
     * @param  entity 输入参数
     * @return 成功与否信息
     */
    @PostMapping("/save")
    public ResponseData<String> save(@ModelAttribute T entity) {
        try {
            service.save(entity);
            return ResponseDataUtil.buildSuccess();
        }
        catch(DuplicateKeyException e)
        {
            return ResponseDataUtil.buildSend(ResultEnums.DUPLICATE_DATA_INPUT);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 修改
     * @param  entity 输入信息
     * @return 成功与否信息
     */
    @PutMapping("/updateById")
    public ResponseData<String> updateById(@ModelAttribute T entity) {
        try {
            service.updateById(entity);
            return ResponseDataUtil.buildSuccess();
        }
        catch(DuplicateKeyException e)
        {
            return ResponseDataUtil.buildSend(ResultEnums.DUPLICATE_DATA_INPUT);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 删除
     *
     * @param id 表id
     * @return 成功与否信息
     */
    @DeleteMapping("/removeById")
    public ResponseData removeById(@RequestParam(value = "id") Long id) {
        try {
            service.removeById(id);
            return ResponseDataUtil.buildSuccess();
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 通过Id查看详情
     *
     * @param id 表id
     * @return 详情信息
     */
    @GetMapping("/detailById")
    public ResponseData<T> detailById(@RequestParam(value = "id") Long id) {
        T entity = service.getById(id);
        if ( null == entity ) {
            return ResponseDataUtil.buildSend(ResultEnums.RESULT_IS_NULL);
        }
        return ResponseDataUtil.buildSuccess(entity);
    }

    /**
     * 获取列表
     *
     * @return 列表
     */
    @GetMapping("/listByEntity")
    public ResponseData<List<T>> listByEntity(@ModelAttribute T entity) {

        List<T> resultEntity = service.list(Wrappers.query(entity).orderByAsc("id"));
        if ( resultEntity.isEmpty() ) {
            return ResponseDataUtil.buildSend(ResultEnums.RESULT_IS_NULL);
        }
        return ResponseDataUtil.buildSuccess(resultEntity);
    }

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/pageByEntity")
    public ResponseData<IPage<T>> pageByEntity(Page<T> page,@ModelAttribute T entity) {
        IPage<T> resultEntity = service.page(page, Wrappers.query(entity).orderByAsc("id"));
        if (resultEntity.getRecords().isEmpty()) {
            return ResponseDataUtil.buildSend(ResultEnums.RESULT_IS_NULL);
        }
        return ResponseDataUtil.buildSuccess(resultEntity);
    }

}
