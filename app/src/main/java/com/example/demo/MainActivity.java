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
 * viewPager grid �ֲ� demo
 * @author fengkun
 *
 */

@ViewUtil.ParentViewInject(R.layout.activity_main)
public class MainActivity extends Activity implements OnPageChangeListener {

	/** viewPager View�б� */
	private List<View> list = new ArrayList<View>();
	/** action��ҳmap */
	private Map<String, List<ActionModel>> actionMap = new HashMap<String, List<ActionModel>>();

	/** viewPagerҳ�� */
	private int pagerCount;
	/** pager���� */
	private int pagerIndex = 0;
	/** viewPagerÿҳgridItem���� */
	private static final int PAGER_GRID_COUNT = 9;
	/** grid column */
	private static final int GRID_COLUMN = 3;
	/** βҳgridItem���� */
	private int c = 0;
	
	/** pager�Ƿ��ֲ� */
	private static final boolean IS_AUTO = true;
	/** pager�ֲ�ʱ���� */
	private static final int GAP = 2 * 1000;
	/** pager�ֲ���ʱ�� */
	private Timer timer;

	/** viewPager */
	@ViewUtil.ChildViewInject(value = R.id.vp, tag = "vp")
	private ViewPager vp;
	/** ָʾ�� */
	@ViewUtil.ChildViewInject(value = R.id.rg, tag = "rg")
	private RadioGroup rg;

	/** viewPager�ֲ�handler */
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
	 * ��ʼ��UI
	 */
	private void init() {
		getRes();
		initPageViewItem();
	}

	/**
	 * ��ȡ��Դ
	 */
	private void getRes() {
		//��Դ����
		String[] res_array = getResources().getStringArray(R.array.actions);
		List<ActionModel> actions = new ArrayList<ActionModel>();
		// action����
		int actionCount = res_array.length;
		c = actionCount % PAGER_GRID_COUNT;
		//pagerҳ��ͳ��
		if (c == 0) {
			pagerCount = actionCount / PAGER_GRID_COUNT;
		} else {
			pagerCount = ((actionCount - c) / PAGER_GRID_COUNT) + 1;
		}

		// ���action
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
		
		//��ҳ
		for(int i=0;i<pagerCount;i++){
			//������㣬�յ�
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
	 * ��ʼ��viewPager Item
	 */
	private void initPageViewItem() {

		for (int i = 0; i < pagerCount; i++) {
			View item = getAdaptedGrid(actionMap.get(i+""));
			// ����viewPager����
			list.add(item);
			// ���ָʾ��
			addNavigation(i);
		}
		GridPagerAdapter grid = new GridPagerAdapter(this);
		grid.setViewList(list);
		vp.setAdapter(grid);
		vp.setOnPageChangeListener(this);
	}

	/**
	 * ���ָʾ��
	 * 
	 * @param index
	 */
	private void addNavigation(final int index) {
		RadioButton rb = new RadioButton(this);
		rg.addView(rb, index);
		if (index == 0) {
			rb.setChecked(true);
		}
		// ���Ч��
		rb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				vp.setCurrentItem(index);
			}
		});

	}

	/**
	 * չʾ����
	 * 
	 * @param index
	 */
	private void show(int index) {
		RadioButton rb = (RadioButton) rg.getChildAt(index);
		rb.setChecked(true);
	}

	/**
	 * ���䵥��pager grid
	 * 
	 * @return
	 */
	@SuppressLint("ShowToast")
	private View getAdaptedGrid(List<ActionModel> list) {
		// viewPagerҳ��
		View item = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.grid, null);
		GridView grid = (GridView) item.findViewById(R.id.grid);
		grid.setNumColumns(GRID_COLUMN);
		final ItemAdapter adapter = new ItemAdapter(list);
		grid.setAdapter(adapter);
		// grid�������
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
	 * action ����ģ��ʱ�޸�
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
		Log.i("��ǰҳ==", pagerIndex + "");
	}

}
