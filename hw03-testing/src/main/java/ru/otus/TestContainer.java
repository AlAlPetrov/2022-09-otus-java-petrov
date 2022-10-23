package ru.otus;

import java.lang.reflect.Method;
import java.util.ArrayList;

public record TestContainer(ArrayList<Method> before, ArrayList<Method> after, Method test) {
}
