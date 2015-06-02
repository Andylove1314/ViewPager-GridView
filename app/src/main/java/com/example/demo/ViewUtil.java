package com.example.demo;

/**
 * ����view ������
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
	 * ���� ע��
	 * @author fengkun
	 *
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ParentViewInject {
		int value();

	}
	
	/**
	 * �ؼ� ע��
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
	 * ��ʼ��activity UI����, ��activity.onCreate()��ִ��
	 * 
	 * @param activity
	 */
	public static void getViews(Activity activity) {
		// �ҵ�����
		setContentView(activity);
		// �ҵ�view
		findViewById(activity);
	}

	/**
	 * ����contentViewע��
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
	 * ����view��ע��
	 */
	public static void findViewById(Activity activity) {
		try {
			Class<?> clazz = activity.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				// �鿴����ֶ��Ƿ��������Զ����ע�����־��
				if (field.isAnnotationPresent(ViewUtil.ChildViewInject.class)) {
					ViewUtil.ChildViewInject inject = field
							.getAnnotation(ViewUtil.ChildViewInject.class);
					int id = inject.value();
					String tag = inject.tag();
					if (id > 0) {
						field.setAccessible(true);
						// ������Ҫ�ҵ��ֶ�����ֵ
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
