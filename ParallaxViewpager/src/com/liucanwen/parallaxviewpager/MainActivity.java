package com.liucanwen.parallaxviewpager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 简单滚动视差组件页面
 * 
 * @author HalfmanHuang
 * @since SDK19 JDK7
 * @version 1.0.0
 */
public class MainActivity extends Activity {
	/** viewpager对象 */
	private ViewPager pager;
	/** viewpager适配器对象 */
	private SimpleAdapter adapter;
	/** 水平滚动条对象（背景图片的容器） */
	private HorizontalScrollView scroll;
	/** 背景图片视图 */
	private ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置布局
		setContentView(R.layout.activity_main);
		// 获取各个组件的实例
		pager = (ViewPager) findViewById(R.id.pager);
		scroll = (HorizontalScrollView) findViewById(R.id.scroll);
		image = (ImageView) findViewById(R.id.image);
		// 实例化适配器
		adapter = new SimpleAdapter(this);

		// 添加测试数据
		addTestData();

		// 将适配器对象设入viewpager
		pager.setAdapter(adapter);
		// 设置viewpager的滚动监听
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// 页面被选择是调用
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// viewpager滑动时被调用

				// pager所有子页面的总宽度
				float widthOfPagers = pager.getWidth() * adapter.getCount();
				// 背景图片的宽的
				float widthOfScroll = image.getWidth();

				// ViewPager可滑动的总长度
				float moveWidthOfPagers = widthOfPagers - pager.getWidth();
				// 背景图的可滑动总长度
				float moveWidthOfScroll = widthOfScroll - pager.getWidth();

				// 可滑动距离比例
				float ratio = moveWidthOfScroll / moveWidthOfPagers;
				// 当前Pager的滑动距离
				float currentPosOfPager = arg0 * pager.getWidth() + arg2;

				// 背景滑动到对应位置
				scroll.scrollTo((int) (currentPosOfPager * ratio),
						scroll.getScrollY());
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// 滚动状态改变时被调用
			}
		});
	}

	/**
	 * 添加测试数据
	 */
	private void addTestData() {
		for (int i = 1; i <= 10; i++) {
			adapter.addPager("第" + i + "个页面");
		}
	}

	/**
	 * 页面viewpager适配器类
	 */
	public class SimpleAdapter extends PagerAdapter {
		/** 子页面列表，注意此处类型为页面描述类 */
		public ArrayList<SimpleHolder> views;
		/** 布局反射器 */
		private LayoutInflater inflater;

		public SimpleAdapter(Context context) {
			// 实例化列表
			views = new ArrayList<SimpleHolder>();
			// 通过系统服务获取布局反射器实例
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// 返回子页面数量
			return views.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// 销毁处理
			container.removeView(views.get(position).root);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 子页面生成处理
			container.addView(views.get(position).root);
			return views.get(position).root;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// 判断生成处理返回的object与某个子页面的关系
			return arg0 == arg1;
		}

		/**
		 * 添加一个子页面
		 */
		public void addPager(String text) {
			// 创建一个子页面描述类对象
			SimpleHolder holder = new SimpleHolder();
			// 通过布局反射器从pageitem_simple获取布局根视图对象
			holder.root = inflater.inflate(R.layout.pageitem, null);
			// 通过根视图对象获取下面的textview子视图
			holder.text = (TextView) holder.root.findViewById(R.id.text);
			// 为textview设置内容
			holder.text.setText(text);
			// 若子页面列表已经初始化
			if (null != views) {
				// 将本子页面描述类设入
				views.add(holder);
			}
		}
	}

	/**
	 * 页面描述类
	 */
	public class SimpleHolder {
		/** 根视图对象 */
		public View root;
		/** 停靠在根视图底部的textview对象 */
		public TextView text;
	}
}
