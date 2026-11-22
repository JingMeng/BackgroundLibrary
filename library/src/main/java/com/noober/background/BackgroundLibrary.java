package com.noober.background;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Field;

public class BackgroundLibrary {

    public static void inject(Context context) {
        LayoutInflater inflater;
        if (context instanceof Activity) {
            inflater = ((Activity) context).getLayoutInflater();
        } else {
            inflater = LayoutInflater.from(context);
        }
        BackgroundFactory factory = new BackgroundFactory();
        if (context instanceof AppCompatActivity) {
            final AppCompatDelegate delegate = ((AppCompatActivity) context).getDelegate();
            factory.setInterceptFactory(new LayoutInflater.Factory() {
                @Override
                public View onCreateView(String name, Context context, AttributeSet attrs) {
                    return delegate.createView(null, name, context, attrs);
                }
            });
        }
        inflater.setFactory(factory);
    }

    /**
     * used for activity which has addFactory
     * 濡傛灉鍥犱负鍏朵粬搴撳凡缁忚缃簡factory锛屽彲浠ヤ娇鐢ㄨ鏂规硶鍘昏繘琛宨nject锛屽湪鍏朵粬搴撶殑setFactory鍚庨潰璋冪敤鍗冲彲
     * @param context
     */
    public static void inject2(Context context) {
        try {
            LayoutInflater inflater;
            if (context instanceof Activity) {
                inflater = ((Activity) context).getLayoutInflater();
            } else {
                inflater = LayoutInflater.from(context);
            }
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(inflater, false);

            BackgroundFactory factory = new BackgroundFactory();
            if (inflater.getFactory2() != null) {
                factory.setInterceptFactory2(inflater.getFactory2());
            } else if (inflater.getFactory() != null) {
                factory.setInterceptFactory(inflater.getFactory());
            }
            inflater.setFactory(factory);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

