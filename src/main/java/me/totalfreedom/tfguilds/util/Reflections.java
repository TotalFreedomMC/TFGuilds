package me.totalfreedom.tfguilds.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflections
{

    public static <T> Object getField(T instance, String name) {
        try
        {
            Field f = instance.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return f.get(instance);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }

     public static <T> Method getMethod(T instance, String name, Class<?>... parameterTypes) {
        try
        {
            Method f = instance.getClass().getDeclaredMethod(name, parameterTypes);
            f.setAccessible(true);
            return f;
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return null;
    }


}
