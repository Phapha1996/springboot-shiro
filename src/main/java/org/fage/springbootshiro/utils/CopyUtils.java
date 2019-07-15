package org.fage.springbootshiro.utils;

import com.github.dozermapper.core.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Caizhf
 * @version 1.0
 * @date 上午11:08 2019/2/27
 * @description bean拷贝工具
 **/
@Component
@Slf4j
public class CopyUtils {

    private static Mapper mapper;

    @Autowired
    public CopyUtils(Mapper mapper) {
        CopyUtils.mapper = mapper;
    }

    /**
     * 拷贝属性名相同的bean
     * 注：使用无参的构造函数
     *
     * @param src
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T copySameProp(Object src, Class<T> targetClass) {
        T result = BeanUtils.instantiateClass(targetClass);
        BeanUtils.copyProperties(src, result);
        return result;
    }

    /**
     * 普通拷贝
     *
     * @param src
     * @param dst
     */
    public static void copySameProp(Object src, Object dst) {
        BeanUtils.copyProperties(src, dst);
    }

    /**
     * 拷贝具有不同属性名称或不同属性类型（注意，需要支持转换）的bean
     * 属性名称不同需要遵循Dozer的配置规则
     * 注：使用无参构造函数
     *
     * @param src
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T copyDifferentProp(Object src, Class<T> targetClass) {
        return mapper.map(src, targetClass);
    }

    /**
     * 拷贝具有不同属性名称或不同属性类型（注意，需要支持转换）的bean
     * 属性名称不同需要遵循Dozer的配置规则
     * 注：使用无参构造函数
     *
     * @param src 拷贝源
     * @param dst 拷贝目标
     * @return
     */
    public static void copyDifferentProp(Object src, Object dst) {
        mapper.map(src, dst);
    }

    /**
     * 将bean转换成Map，只能拷贝基础类型
     *
     * @param src 需要转换的bean
     * @return
     */
    public static Map<String, Object> toMap(Object src) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            if (Objects.nonNull(src)) {
                Class type = src.getClass();
                //获取bean反射信息
                BeanInfo beanInfo = Introspector.getBeanInfo(type);
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

                //对bean中的所有属性进行处理
                for (int i = 0; i < propertyDescriptors.length; i++) {
                    PropertyDescriptor descriptor = propertyDescriptors[i];
                    Class propertyType = descriptor.getPropertyType();
                    String propertyName = descriptor.getName();
                    //保证所有的属性不是null
                    if (!propertyName.equals("class")) {
                        //属性名首字母转换成大写
                        Method readMethod = descriptor.getReadMethod();
                        Object result = readMethod.invoke(src, null);
                        if (Objects.nonNull(result)) {
                            if (isBaseType(result)) {
                                //如果该属性是基础类型，直接设置进map
                                returnMap.put(propertyName, result);
                            } else if (result instanceof List) {
                                //如果该属性是list类型，将其递归设置成List<Map>
                                List<Map<String, Object>> listMap = new ArrayList<>();
                                for (Object o : (Collection) result) {
                                    listMap.add(toMap(o));
                                }
                                returnMap.put(propertyName, listMap);
                            } else if (result instanceof Map) {
                                //如果该属性为Map类型，直接设置为Map
                                returnMap.put(propertyName, result);
                            } else {
                                //如果该属性是bean类型，将其递归解析成map
                                returnMap.put(propertyName, toMap(result));
                            }
                        } else {
                            putDefaultValue(returnMap, propertyType, propertyName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("将bean={}拷贝至map时出错", src.toString());
        }
        return returnMap;
    }


    /**
     * 判断object是否为基本类型
     *
     * @param object
     * @return
     */
    private static boolean isBaseType(Object object) {
        Class className = object.getClass();
        if (className.equals(Integer.class) ||
                className.equals(Byte.class) ||
                className.equals(Long.class) ||
                className.equals(Double.class) ||
                className.equals(Float.class) ||
                className.equals(Character.class) ||
                className.equals(Short.class) ||
                className.equals(Boolean.class) ||
                className.equals(String.class)) {
            return true;
        }
        return false;
    }

    /**
     * 当值为空时，给其默认设置一个属性
     * @param returnMap
     * @param propertyName
     */
    private static void putDefaultValue(Map<String, Object> returnMap, Class propertyType, String propertyName) {
        if(propertyType.equals(Integer.class) ||  propertyType.equals(Long.class)
                || propertyType.equals(Short.class) || propertyType.equals(Byte.class)
                || propertyType.equals(BigDecimal.class)){
            returnMap.put(propertyName, 0);
        }else if(propertyType.equals(Double.class) || propertyType.equals(Float.class)){
            returnMap.put(propertyName, 0.0);
        }else if(propertyType.equals(Character.class) || propertyType.equals(String.class)){
            returnMap.put(propertyName, "");
        }else if(propertyType.equals(Boolean.class)){
            returnMap.put(propertyName, false);
        }else if(propertyType.equals(List.class)){
            returnMap.put(propertyName, new ArrayList());
        }else if(propertyType.equals(Set.class)){
            returnMap.put(propertyName, new HashSet());
        }else if(propertyType.equals(Map.class)){
            returnMap.put(propertyName, new HashMap());
        }else {
           log.error("无法转换成基本的Map，该字段{}基本类型为{}", propertyName, propertyType);
        }
    }

}
