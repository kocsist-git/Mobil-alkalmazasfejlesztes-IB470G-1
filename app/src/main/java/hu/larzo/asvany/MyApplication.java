package hu.larzo.asvany;

import android.app.Application;

public class MyApplication extends Application {
    private static Cart cart;

    @Override
    public void onCreate() {
        super.onCreate();
        if (cart == null) {
            cart = new Cart();
        }
    }

    public static Cart getInstance() {
        if (cart == null) {
            synchronized (MyApplication.class) {
                if (cart == null) {
                    cart = new Cart();
                }
            }
        }
        return cart;
    }
}
