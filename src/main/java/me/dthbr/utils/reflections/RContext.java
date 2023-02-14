package me.dthbr.utils.reflections;

public class RContext {

    private final Class<?> returnType;
    private final String name;
    private final int index;

    public RContext(Class<?> returnType, String name) {
        this(returnType, name, -1);
    }

    public RContext(Class<?> returnType, int index) {
        this(returnType, null, index);
    }

    public RContext(Class<?> returnType, String name, int index) {
        this.returnType = returnType;
        this.name = name;
        this.index = index;
    }

    public boolean compareIndex(RContext other) {
        return returnType == other.returnType && index == other.index;
    }

    public boolean compareName(RContext other) {
        return returnType == other.returnType && name.equals(other.name);
    }

    public boolean compare(RContext other) {
        return returnType == other.returnType && (name == null || name.equals(other.name)) && (index == -1 || index == other.index);
    }

    public RContext withName(String name) {
        return new RContext(returnType, name, index);
    }

    public RContext withIndex(int index) {
        return new RContext(returnType, name, index);
    }

}
