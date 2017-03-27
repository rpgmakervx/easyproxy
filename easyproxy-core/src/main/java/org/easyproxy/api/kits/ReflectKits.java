package org.easyproxy.api.kits;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.Collection;

/**
 * Description :
 * Created by code4j on 16-11-3
 * 上午12:36
 */

public class ReflectKits {

    public static Object newInstance(String classname) {
        try {
            Class clazz = Class.forName(classname);
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object newInstance(Class<?> clazz, Class[] parameterTypes, Object[] initargs) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);
            return constructor.newInstance(initargs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object newInstanceViolently(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object newInstanceViolently(Class<?> clazz, Class[] parameterTypes, Object[] initargs) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(initargs);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    public static void setFieldValue(Object obj, String propertyName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    public static Object getter(Object object, String propertyName) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(propertyName, object.getClass());
            Method method = pd.getReadMethod();
            if (method == null) {
                throw new IllegalArgumentException("method may not exists");
            }
            return method.invoke(object);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    public static void setter(Object object, String propertyName, Object value) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(propertyName, object.getClass());
            Method method = pd.getWriteMethod();
            if (method == null) {
                throw new IllegalArgumentException("method may not exists");
            }
            method.invoke(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置字段值
     *
     * @param className      类的全路径名称
     * @param methodName     调用方法名
     * @param parameterTypes 参数类型
     * @param values         参数值
     * @param object         实例对象
     * @return
     */
    public static Object methodInvoke(String className, String methodName, Class[] parameterTypes, Object[] values, Object object) {
        try {
            Method method = Class.forName(className).getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(object, values);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    public static PropertyDescriptor[] propertyDescriptors(Class<?> c) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(c);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return beanInfo.getPropertyDescriptors();
    }

    public static<T> Class<T> getGenricType(T bean){
        return (Class<T>)getSuperClassGenricType(bean.getClass(), 0);
    }

    public static Class<Object> getSuperClassGenricType(final Class clazz, final int index) {
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }


    public static Class getReturnType(Method method){
        Type returnType = method.getGenericReturnType();// 返回类型
        return method.getReturnType();
    }
    public static Class getGenericReturnType(Method method){
        Type returnType = method.getGenericReturnType();// 返回类型
        if (returnType instanceof ParameterizedType)/**//* 如果是泛型类型 */{
            Type[] types = ((ParameterizedType) returnType)
                    .getActualTypeArguments();// 泛型类型列表
            for (Type type : types) {
                return (Class) type;
            }
        }
        return method.getReturnType();
    }

    public static boolean isBaseType(Class clz) {
        try {
            return (clz.isPrimitive())||((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 常用类型包括基本类型，包装类型和字符串类型
     * @param clz
     * @return
     */
    public static boolean isFrequentlyUseType(Class clz){
        return isBaseType(clz)||clz.equals(String.class);
    }

    /**
     * 判断集合是否是统一的类型
     * @param col
     * @param cls
     * @return
     */
    public static boolean isUnifiedCollection(Collection col,Class<?> cls){
        for (Object obj:col){
            if (!obj.getClass().equals(cls)){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断数组是否是统一的类型
     * @param col
     * @param cls
     * @return
     */
    public static boolean isUnifiedCollection(Object[] col,Class<?> cls){
        for (Object obj:col){
            if (!obj.getClass().equals(cls)){
                return false;
            }
        }
        return true;
    }

}
