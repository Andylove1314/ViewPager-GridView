package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * viewPager grid �1�7�0�2�1�7 demo
 * @author fengkun
 *
 */

@ViewUtil.ParentViewInject(R.layout.activity_main)
public class MainActivity extends Activity implements OnPageChangeListener {

	/** viewPager View�1�7�҄1�7 */
	private List<View> list = new ArrayList<View>();
	/** action�1�7�1�7�0�7map */
	private Map<String, List<ActionModel>> actionMap = new HashMap<String, List<ActionModel>>();

	/** viewPager�0�7�1�7�1�7 */
	private int pagerCount;
	/** pager�1�7�1�7�1�7�1�7 */
	private int pagerIndex = 0;
	/** viewPager�0�7�0�7gridItem�1�7�1�7�1�7�1�7 */
	private static final int PAGER_GRID_COUNT = 9;
	/** grid column */
	private static final int GRID_COLUMN = 3;
	/** �0�7gridItem�1�7�1�7�1�7�1�7 */
	private int c = 0;
	
	/** pager�1�7�0�9�1�7�1�7�0�2�1�7 */
	private static final boolean IS_AUTO = true;
	/** pager�1�7�0�2�1�7�0�2�1�7�1�7�1�7�1�7 */
	private static final int GAP = 2 * 1000;
	/** pager�1�7�0�2�1�7�1�7�1�7�0�2�1�7�1�7 */
	private Timer timer;

	/** viewPager */
	@ViewUtil.ChildViewInject(value = R.id.vp, tag = "vp")
	private ViewPager vp;
	/** �0�8�0�5�1�7�1�7 */
	@ViewUtil.ChildViewInject(value = R.id.rg, tag = "rg")
	private RadioGroup rg;

	/** viewPager�1�7�0�2�1�7handler */
	private Handler handle = new Handler() {
		public void handleMessage(Message msg) {
			vp.setCurrentItem(msg.what);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtil.getViews(this);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!IS_AUTO){
			return;
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handle.sendEmptyMessage(pagerIndex++);
			}
		}, 0, GAP);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(timer!=null){
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * �1�7�1�7�0�3�1�7�1�7UI
	 */
	private void init() {
		getRes();
		initPageViewItem();
	}

	/**
	 * �1�7�1�7�0�0�1�7�1�7�0�6
	 */
	private void getRes() {
		//�1�7�1�7�0�6�1�7�1�7�1�7�1�7
		String[] res_array = getResources().getStringArray(R.array.actions);
		List<ActionModel> actions = new ArrayList<ActionModel>();
		// action�1�7�1�7�1�7�1�7
		int actionCount = res_array.length;
		c = actionCount % PAGER_GRID_COUNT;
		//pager�0�7�1�7�1�7�0�1�1�7�1�7
		if (c == 0) {
			pagerCount = actionCount / PAGER_GRID_COUNT;
		} else {
			pagerCount = ((actionCount - c) / PAGER_GRID_COUNT) + 1;
		}

		// �1�7�1�7�1�7action
		for (int i = 0; i < actionCount; i++) {
			ActionModel action = new ActionModel();
			String titNmae = res_array[i].split("\\|")[0];
			String draNmae = res_array[i].split("\\|")[1];
			int id = getResources().getIdentifier(draNmae, "drawable",
					getPackageName());
			Drawable dra = getResources().getDrawable(id);
			int actIndex = Integer.parseInt(res_array[i].split("\\|")[2]);
			action.setTitleName(titNmae);
			action.setDrawable(dra);
			action.setActionIndex(actIndex);
			actions.add(action);
		}
		
		//�1�7�1�7�0�7
		for(int i=0;i<pagerCount;i++){
			//�1�7�1�7�1�7�1�7�1�7�1�7�0�4�1�7�0�1�1�7
			int start = i*PAGER_GRID_COUNT;
			int stop = start+PAGER_GRID_COUNT;
			List<ActionModel> pagerActions = null;
			if(c==0){
				pagerActions = actions.subList(start, stop);
			}else{
				if(i==pagerCount-1){
					pagerActions = actions.subList(start, actionCount);
				}else{
					pagerActions = actions.subList(start, stop);
				}
			}
			actionMap.put(i+"", pagerActions);	
		}
		

	}

	/**
	 * �1�7�1�7�0�3�1�7�1�7viewPager Item
	 */
	private void initPageViewItem() {

		for (int i = 0; i < pagerCount; i++) {
			View item = getAdaptedGrid(actionMap.get(i+""));
			// �1�7�1�7�1�7�1�7viewPager�1�7�1�7�1�7�1�7
			list.add(item);
			// �1�7�1�7�1�7�0�8�0�5�1�7�1�7
			addNavigation(i);
		}
		GridPagerAdapter grid = new GridPagerAdapter(this);
		grid.setViewList(list);
		vp.setAdapter(grid);
		vp.setOnPageChangeListener(this);
	}

	/**
	 * �1�7�1�7�1�7�0�8�0�5�1�7�1�7
	 * 
	 * @param index
	 */
	private void addNavigation(final int index) {
		RadioButton rb = new RadioButton(this);
		rg.addView(rb, index);
		if (index == 0) {
			rb.setChecked(true);
		}
		// �1�7�1�7�1�7���1�7�1�7
		rb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				vp.setCurrentItem(index);
			}
		});

	}

	/**
	 * �0�5�0�5�1�7�1�7�1�7�1�7
	 * 
	 * @param index
	 */
	private void show(int index) {
		RadioButton rb = (RadioButton) rg.getChildAt(index);
		rb.setChecked(true);
	}

	/**
	 * �1�7�1�7�1�7�4�9�1�7�1�7pager grid
	 * 
	 * @return
	 */
	@SuppressLint("ShowToast")
	private View getAdaptedGrid(List<ActionModel> list) {
		// viewPager�0�7�1�7�1�7
		View item = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.grid, null);
		GridView grid = (GridView) item.findViewById(R.id.grid);
		grid.setNumColumns(GRID_COLUMN);
		final ItemAdapter adapter = new ItemAdapter(list);
		grid.setAdapter(adapter);
		// grid�1�7�1�7�1�7�1�7�1�7�1�7�1�7
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				TextView textView = (TextView) view.findViewById(R.id.text);
				int actionIndex = (Integer) textView.getTag();
				String title = textView.getText().toString();
				go(actionIndex, title);
			}
		});
		return item;
	}

	/**
	 * action �1�7�1�7�1�7�1�7�0�0�1�7�1�7�0�2�1�7�1�0�1�7
	 */
	private void go(int actionIndex, String title){
		
		switch (actionIndex) {
		case 0:
			Intent in = new Intent(MainActivity.this,Action01Activity.class);
			in.putExtra("action", title);
			startActivity(in);
			break;

		default:
			Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	/**
	 * grid
	 * 
	 * @author fengkun
	 */
	private class ItemAdapter extends BaseAdapter {

		private List<ActionModel> action;

		public ItemAdapter(List<ActionModel> action) {
			ItemAdapter.this.action = action;
		}

		@Override
		public int getCount() {
			return action.size();
		}

		@Override
		public Object getItem(int index) {
			return null;
		}

		@Override
		public long getItemId(int index) {
			return 0;
		}

		@Override
		public View getView(int index, View arg1, ViewGroup arg2) {
			arg1 = LayoutInflater.from(MainActivity.this).inflate(
					R.layout.item_grid, null);
			ImageView image = (ImageView) arg1.findViewById(R.id.drawable);
			image.setBackgroundDrawable(action.get(index).getDrawable());
			TextView text = (TextView) arg1.findViewById(R.id.text);
			text.setText(action.get(index).getTitleName());
			text.setTag(action.get(index).getActionIndex());
			return arg1;
		}

	}

	@Override
	public void onPageScrollStateChanged(int index) {

	}

	@Override
	public void onPageScrolled(int index, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int index) {
		show(index);
		pagerIndex = index;
		if(pagerIndex==pagerCount-1){
			pagerIndex = 0;
		}
		Log.i("�1�7�1�7�0�2�0�7==", pagerIndex + "");
	}

}
