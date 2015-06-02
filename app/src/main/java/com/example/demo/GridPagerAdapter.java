package com.example.demo;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * viewPager  ≈‰∆˜
 * 
 * @author fengkun
 * 
 */
public class GridPagerAdapter extends PagerAdapter {

	private static String TAG = GridPagerAdapter.class.getName();
	private Context context;
	private List<View> list;

	public GridPagerAdapter(Context context) {
		this.context = context;
	}

	public void setViewList(List<View> list) {
		notifyDataSetChanged();
		this.list = list;
		
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(list.get(position), 0);
		return list.get(position);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	} 
	
}
