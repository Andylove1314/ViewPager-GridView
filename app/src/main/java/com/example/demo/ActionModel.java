package com.example.demo;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

/**
 * action
 * @author fengkun
 *
 */
public class ActionModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Drawable drawable;
	private String titleName;
	private int actionIndex;
	
	public Drawable getDrawable() {
		return drawable;
	}
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	public int getActionIndex() {
		return actionIndex;
	}
	public void setActionIndex(int actionIndex) {
		this.actionIndex = actionIndex;
	}
	
	
}
