package com.example.demo;

/**
 * 布局view 工具类
 * @author fengkun
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import android.app.Activity;
import android.view.View;

public class ViewUtil {

	/**
	 * 布局 注解
	 * @author fengkun
	 *
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ParentViewInject {
		int value();

	}
	
	/**
	 * 控件 注解
	 * @author fengkun
	 *
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ChildViewInject {
		int value();
		String tag();
	}

	/**
	 * 初始化activity UI布局, 在activity.onCreate()中执行
	 * 
	 * @param activity
	 */
	public static void getViews(Activity activity) {
		// 找到布局
		setContentView(activity);
		// 找到view
		findViewById(activity);
	}

	/**
	 * 解析contentView注解
	 */
	public static void setContentView(Activity activity) {
		try {
			Class<?> clazz = activity.getClass();
			if (clazz.isAnnotationPresent(ViewUtil.ParentViewInject.class)) {
				ViewUtil.ParentViewInject inject = clazz
						.getAnnotation(ViewUtil.ParentViewInject.class);
				int layout = inject.value();
				activity.setContentView(layout);
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析view域注解
	 */
	public static void findViewById(Activity activity) {
		try {
			Class<?> clazz = activity.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				// 查看这个字段是否有我们自定义的注解类标志的
				if (field.isAnnotationPresent(ViewUtil.ChildViewInject.class)) {
					ViewUtil.ChildViewInject inject = field
							.getAnnotation(ViewUtil.ChildViewInject.class);
					int id = inject.value();
					String tag = inject.tag();
					if (id > 0) {
						field.setAccessible(true);
						// 给我们要找的字段设置值
						View view = activity.findViewById(id);
						view.setTag(tag);
						field.set(activity, view);
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
}
