package jpql.generic;

import java.util.Arrays;

public class Generic<T> {
    private T object;

    public Generic(T object) {
        this.object = object;
        print();
    }

    private void print() {
        System.out.println("=====================");
        if (object instanceof Object[]) {
            printAll();
            return;
        }
        printOne();
        System.out.println("=====================");
    }

    private void printAll() {

        Object[] objects = (Object[]) object;
        for (Object result : objects) {
            if (result != null) {
                System.out.println(result.getClass().getName() + "\t: result = " + result);
                continue;
            }
            System.out.println("result = null");
        }
    }

    private void printOne() {
        StringBuilder sb = new StringBuilder();

        T result = object;
        if (result != null) {
            System.out.println(result.getClass().getName() + "\t, result = " + result);
            return;
        }
        System.out.println("result = null");
    }
}