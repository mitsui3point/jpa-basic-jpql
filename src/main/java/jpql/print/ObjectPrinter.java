package jpql.print;

public class ObjectPrinter {
    private Object object;

    public ObjectPrinter(Object object) {
        this.object = object;
    }

    public void print() {
        System.out.println("=====================");
        if (isArray()) {
            printAll();
            return;
        }
        printOne();
        System.out.println("=====================");
    }

    private boolean isArray() {
        return object instanceof Object[];
    }

    private void printAll() {

        Object[] objects = (Object[]) object;
        for (Object object : objects) {
            if (object != null) {
                System.out.println(object.getClass().getName() +
                        "\t: result = " + object);
                continue;
            }
            System.out.println(NullPointerException.class.getName() + "\t, result = null");
        }
    }

    private void printOne() {
        StringBuilder sb = new StringBuilder();

        if (object != null) {
            System.out.println(object.getClass().getName() + "\t, result = " + object);
            return;
        }
        System.out.println(NullPointerException.class.getName() + "\t, result = null");
    }
}