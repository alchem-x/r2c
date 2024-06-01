package alchem.r2c;

public abstract class Console {

    public static void log(Object o) {
        System.out.println(o);
    }

    public static void error(Object o) {
        System.err.println(o);
    }
}
