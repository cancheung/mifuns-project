package com.mifuns.common.web.advice;

import com.mifuns.common.util.DateUtils;
import com.mifuns.common.web.contants.ResponseConstant;
import com.mifuns.common.web.entity.ResponseEntity;
import org.slf4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by miguangying on 2017/3/11.
 */
public abstract class AbstractExceptionControllerAdvice {
    public abstract Logger getLogger();

    /**
     * Valid Validated 注解错误
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object notValid(MethodArgumentNotValidException e){
        getLogger().error(e.getParameter().getMethod() + "参数校验失败", e);
        return processBindException(e.getBindingResult(),true);
    }

    /**
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Object bindEx(BindException e){
        getLogger().error(e.getObjectName() + "参数校验失败", e);
        return processBindException(e.getBindingResult(),true);
    }
    private ResponseEntity<Map> processBindException(BindingResult bindingResult){
        ResponseEntity<Map> responseEntity = new ResponseEntity<>();
        responseEntity.setData(Collections.EMPTY_MAP);
        responseEntity.setResponseCode(ResponseConstant.CODE_PARAMETER_INVALID);
        List<FieldError> errors = bindingResult.getFieldErrors();

        boolean first = true;
        StringBuilder sb = new StringBuilder("Validation error(s) [ ");
        for (FieldError fieldError : errors) {
            if(first){
                sb.append(fieldError.getField()).append(" = ").append(fieldError.getDefaultMessage());
                first = false;
            }else {
                sb.append(" ,").append(fieldError.getField()).append(" = ").append(fieldError.getDefaultMessage());
            }
        }
        sb.append("]");
        responseEntity.setResponseMessage(sb.toString());
        responseEntity.setResponseDate(DateUtils.now());
        return responseEntity;
    }

    private ResponseEntity<Map> processBindException(BindingResult bindingResult, boolean showFirst){
        ResponseEntity<Map> responseEntity = new ResponseEntity<>();
        responseEntity.setData(Collections.EMPTY_MAP);
        responseEntity.setResponseCode(ResponseConstant.CODE_PARAMETER_INVALID);
        List<FieldError> errors = bindingResult.getFieldErrors();
        if(!showFirst){
            return processBindException(bindingResult);
        }
        responseEntity.setResponseMessage(errors.get(0).getDefaultMessage());
        responseEntity.setResponseDate(DateUtils.now());
        return responseEntity;
    }

    /**
     * 参数校验错误
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Object illegalArgument(IllegalArgumentException e){
        getLogger().error("参数校验失败", e);
        ResponseEntity<Map> responseEntity = new ResponseEntity<Map>();
        responseEntity.setData(Collections.EMPTY_MAP);
        responseEntity.setResponseCode(ResponseConstant.CODE_PARAMETER_INVALID);
        responseEntity.setResponseMessage(ResponseConstant.MSG_PARAMETER_INVALID);
        responseEntity.setResponseDate(DateUtils.now());
        return responseEntity;
    }

    /**
     * Exception 默认异常返回
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object defaulted(Exception e){
        getLogger().error("其他异常", e);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>();
        responseEntity.setData(Collections.EMPTY_MAP);
        responseEntity.setResponseCode(ResponseConstant.CODE_ERROR_INVALID);
        responseEntity.setResponseMessage(ResponseConstant.MSG_ERROR_INVALID);
        responseEntity.setResponseDate(DateUtils.now());
        return responseEntity;
    }
}
