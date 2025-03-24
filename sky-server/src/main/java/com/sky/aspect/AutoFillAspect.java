package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.reflect.MethodSignature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/03/24/17:02
 * @Description:自定义切面，实现自定义公共字段填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    //@Before:前置通知，此注解标注的通知方法在目标方法前被执行
    //在我们执行insert、update方法之前，执行公共字段填充
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段填充...");
        //1.获取到当前被拦截的方法上数据库的操作类型

        // 获取到当前被拦截的方法的签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获得到当前被拦截的方法的注解对象
        AutoFill autoFill= signature.getMethod().getAnnotation(AutoFill.class);
        //获取到当前被拦截的方法的注解对象上的value属性-数据库的操作类型
        OperationType value = autoFill.value();



        //2.获取到当前被拦截的方法的参数
        Object[] args = joinPoint.getArgs();
        if (args==null || args.length==0){
            return ;
        }
        Object entity = args[0];

        //3.准备赋值的数据（时间和当前用户的id）
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();


        //4.根据当前不同的数据库操作类型，进行不同的赋值操作（反射)
        if (value.equals(OperationType.INSERT)){
            //给entity对象中的createTime和updateTime属性赋值
            //给entity对象中的createUser和updateUser属性赋值
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);



                //通过反射为对象属性赋值
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (value.equals(OperationType.UPDATE)){
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);


                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
