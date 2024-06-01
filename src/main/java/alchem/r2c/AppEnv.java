package alchem.r2c;

public abstract class AppEnv {

    private static String e(String key, String defaultValue) {
        return System.getenv().getOrDefault(key, defaultValue);
    }

    public static final String R2_ACCESS_KEY = e("R2_ACCESS_KEY", "");
    public static final String R2_SECRET_KEY = e("R2_SECRET_KEY", "");
    public static final String R2_ENDPOINT = e("R2_ENDPOINT", "");
    public static final String R2_BUCKET = e("R2_BUCKET", "");
    public static final String R2_DIST = e("R2_DIST", "dist");
    public static final String R2C = e("R2C", "");
}
