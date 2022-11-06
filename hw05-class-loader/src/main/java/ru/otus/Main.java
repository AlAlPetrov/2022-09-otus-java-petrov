package ru.otus;

/*
    java -javaagent:classLoader.jar -jar classLoader.jar
*/


public class Main {
    public static void main(String[] args) {
        var calculator = new Calculator();
        calculator.MultiplyByTwo(123);
        calculator.MultiplyByTwo(123, 456);
        calculator.MultiplyByTwo(123, 456, 789);
    }
}
