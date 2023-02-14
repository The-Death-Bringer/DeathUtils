package me.dthbr.utils.reflections;

import java.lang.invoke.MethodHandles;

public class Reflections {

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    public static MethodHandles.Lookup lookup() {
        return LOOKUP;
    }

}
