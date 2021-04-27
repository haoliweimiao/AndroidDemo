package com.hlw.demo.hook;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RefInvoke {

    /**
     * 创建一个类
     *
     * @param className  创建类的名称
     * @param pareTypes  创建类构造函数类型
     * @param pareValues 创建类构造函数数值
     * @return 创建的类
     * Class r = Class.forName(className);
     * // 含参
     * Class[] p3 = {int.class, String.class};
     * Object[] v3 = {1, "test_value"};
     * Object obj = RefInvoke.createObject(r, p3, v3);
     * // 无参
     * Object obj2 = RefInvoke.createObject(r, null, null);
     */
    public static Object createObject(String className, Class[] pareTypes, Object[] pareValues) {
        try {
            Class r = Class.forName(className);
            Constructor ctor = r.getDeclaredConstructor(pareTypes);
            ctor.setAccessible(true);
            return ctor.newInstance(pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 调用实例方法
     *
     * @param obj        调用方法的对象
     * @param methodName 方法名称
     * @param pareTypes  方法参数类型
     * @param pareValues 方法参数
     * @return 调用方法
     * Class[] p3 = {};
     * Object[] v3 = {};
     * ReInvoke.invokeInstanceMethod(className, "methodName", p3, v3);
     */
    public static Object invokeInstanceMethod(Object obj, String methodName, Class[] pareTypes, Object[] pareValues) {
        if (obj == null)
            return null;

        try {
            // 调用一个private方法
            // 在指定类中获取指定的方法
            Method method = obj.getClass().getDeclaredMethod(methodName, pareTypes);
            method.setAccessible(true);
            return method.invoke(obj, pareValues);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 调用静态方法
     *
     * @param className  类名称
     * @param methodName 方法名称
     * @param pareTypes  方法参数类型
     * @param pareValues 方法参数
     * @return the result of dispatching the method represented by
     * this object on {@code obj} with parameters
     * <p>
     * Class[] p4 = {String.class};
     * Object[] v4 = {"test_value"};
     * Object result = RefInvoke.invokeStaticMethod("test_method", p4, v4);
     */
    public static Object invokeStaticMethod(String className, String methodName, Class[] pareTypes, Object[] pareValues) {
        try {
            Class obj_class = Class.forName(className);
            Method method = obj_class.getDeclaredMethod(methodName, pareTypes);
            method.setAccessible(true);
            return method.invoke(null, pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取一个字段的值
     *
     * @param className 类名称
     * @param obj       从中获取值的对象
     * @param filedName 字段名称
     * @return the value of the represented field in object
     *
     * // 获取实例字段
     * Object fieldObject = RefInvoke.getFieldObject(className, obj, "name");
     * RefInvoke.setFieldObject(className, obj, "name", "set_name");
     *
     * // 获取静态字段
     * Object fieldObject = RefInvoke.getFieldObject(className, obj, "address");
     * RefInvoke.setFieldObject(className, obj, "name", "set_address");
     */
    public static Object getFieldObject(String className, Object obj, String filedName) {
        try {
            Class objClass = Class.forName(className);
            Field field = objClass.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 设置一个字段的值
     *
     * @param className  类名称
     * @param obj
     * @param filedName  字段名称
     * @param filedValue 设置的值
     * @return the value of the represented field in object
     */
    public static void setFieldObject(String className, Object obj, String filedName, Object filedValue) {
        try {
            Class objClass = Class.forName(className);
            Field field = objClass.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, filedValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //无参
    public static Object createObject(String className) {
        Class[] pareTyples = new Class[]{};
        Object[] pareVaules = new Object[]{};

        try {
            Class r = Class.forName(className);
            return createObject(r, pareTyples, pareVaules);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    //无参
    public static Object createObject(Class clazz) {
        Class[] pareTyple = new Class[]{};
        Object[] pareVaules = new Object[]{};

        return createObject(clazz, pareTyple, pareVaules);
    }

    //一个参数
    public static Object createObject(String className, Class pareTyple, Object pareVaule) {
        Class[] pareTyples = new Class[]{pareTyple};
        Object[] pareVaules = new Object[]{pareVaule};

        try {
            Class r = Class.forName(className);
            return createObject(r, pareTyples, pareVaules);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    //一个参数
    public static Object createObject(Class clazz, Class pareTyple, Object pareVaule) {
        Class[] pareTyples = new Class[]{pareTyple};
        Object[] pareVaules = new Object[]{pareVaule};

        return createObject(clazz, pareTyples, pareVaules);
    }


    //多个参数
    public static Object createObject(Class clazz, Class[] pareTyples, Object[] pareVaules) {
        try {
            Constructor ctor = clazz.getDeclaredConstructor(pareTyples);
            ctor.setAccessible(true);
            return ctor.newInstance(pareVaules);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //一个参数
    public static Object invokeInstanceMethod(Object obj, String methodName, Class pareTyple, Object pareVaule) {
        Class[] pareTyples = {pareTyple};
        Object[] pareVaules = {pareVaule};

        return invokeInstanceMethod(obj, methodName, pareTyples, pareVaules);
    }

    //无参
    public static Object invokeInstanceMethod(Object obj, String methodName) {
        Class[] pareTyples = new Class[]{};
        Object[] pareVaules = new Object[]{};

        return invokeInstanceMethod(obj, methodName, pareTyples, pareVaules);
    }


    //无参
    public static Object invokeStaticMethod(String className, String method_name) {
        Class[] pareTyples = new Class[]{};
        Object[] pareVaules = new Object[]{};

        return invokeStaticMethod(className, method_name, pareTyples, pareVaules);
    }

    //一个参数
    public static Object invokeStaticMethod(String className, String method_name, Class pareTyple, Object pareVaule) {
        Class[] pareTyples = new Class[]{pareTyple};
        Object[] pareVaules = new Object[]{pareVaule};

        return invokeStaticMethod(className, method_name, pareTyples, pareVaules);
    }

    //无参
    public static Object invokeStaticMethod(Class clazz, String method_name) {
        Class[] pareTyples = new Class[]{};
        Object[] pareVaules = new Object[]{};

        return invokeStaticMethod(clazz, method_name, pareTyples, pareVaules);
    }

    //一个参数
    public static Object invokeStaticMethod(Class clazz, String method_name, Class classType, Object pareVaule) {
        Class[] classTypes = new Class[]{classType};
        Object[] pareVaules = new Object[]{pareVaule};

        return invokeStaticMethod(clazz, method_name, classTypes, pareVaules);
    }

    //多个参数
    public static Object invokeStaticMethod(Class clazz, String method_name, Class[] pareTyples, Object[] pareVaules) {
        try {
            Method method = clazz.getDeclaredMethod(method_name, pareTyples);
            method.setAccessible(true);
            return method.invoke(null, pareVaules);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    //简写版本
    public static Object getFieldObject(Object obj, String filedName) {
        return getFieldObject(obj.getClass(), obj, filedName);
    }

    public static Object getFieldObject(Class clazz, Object obj, String filedName) {
        try {
            Field field = clazz.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //简写版本
    public static void setFieldObject(Object obj, String filedName, Object filedVaule) {
        setFieldObject(obj.getClass(), obj, filedName, filedVaule);
    }

    public static void setFieldObject(Class clazz, Object obj, String filedName, Object filedVaule) {
        try {
            Field field = clazz.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, filedVaule);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getStaticFieldObject(String className, String filedName) {
        return getFieldObject(className, null, filedName);
    }

    public static Object getStaticFieldObject(Class clazz, String filedName) {
        return getFieldObject(clazz, null, filedName);
    }

    public static void setStaticFieldObject(String classname, String filedName, Object filedVaule) {
        setFieldObject(classname, null, filedName, filedVaule);
    }

    public static void setStaticFieldObject(Class clazz, String filedName, Object filedVaule) {
        setFieldObject(clazz, null, filedName, filedVaule);
    }
}