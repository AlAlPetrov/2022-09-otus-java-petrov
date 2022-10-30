package ru.otus;

import java.lang.reflect.Method;
import java.util.List;

public record TestContainer(List<Method> before, List<Method> after, Method test) {
}
