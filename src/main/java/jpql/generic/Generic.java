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
            System.out.println(printAll());
            return;
        }
        System.out.println(printOne());
    }

    private String printAll() {
        StringBuilder sb = new StringBuilder();

        Object[] objects = (Object[]) object;
        for (Object result : objects) {
            if (result != null) {
                sb.append(result.getClass().getName() + "\t: result = " + result + "\n");
                continue;
            }
            sb.append("result = null");
        }

        return sb.toString();
    }

    private String printOne() {
        StringBuilder sb = new StringBuilder();

        T result = object;
        if (result != null) {
            return sb.append(result.getClass().getName() + "\t, result = " + result).toString();
        }
        sb.append("result = null");

        return sb.toString();
    }
}