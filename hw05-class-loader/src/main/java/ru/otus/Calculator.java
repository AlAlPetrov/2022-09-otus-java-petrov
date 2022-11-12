package ru.otus;

import ru.otus.annotations.Log;

public class Calculator {
    @Log(fields = { "0"})
    public int MultiplyByTwo(int first) {
        System.out.println("MultiplyByTwo with 1 argument has been invoked");
        return first * 2;
    }

    public int MultiplyByTwo(int first, int second) {
        System.out.println("MultiplyByTwo with 2 arguments has been invoked");
        return first * 2;
    }

    @Log(fields = { "0", "2"})
    public int MultiplyByTwo(int first, int second, int third) {
        System.out.println("MultiplyByTwo with 3 arguments has been invoked");
        return first * 2;
    }
}
