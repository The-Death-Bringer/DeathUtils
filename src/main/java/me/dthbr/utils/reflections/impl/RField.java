package me.dthbr.utils.reflections.impl;

import java.lang.invoke.MethodHandle;

public class RField {

    private final MethodHandle getter;
    private final MethodHandle setter;

    public RField(MethodHandle getter, MethodHandle setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public <T> T get(Object instance) {
        try {
            return (T) getter.invoke(instance);
        } catch (Throwable e) {
            return null;
        }
    }

    public void set(Object instance, Object value) {
        try {
            setter.invoke(instance, value);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
