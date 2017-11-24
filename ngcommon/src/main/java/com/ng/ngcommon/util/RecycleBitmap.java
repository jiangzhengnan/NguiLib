package com.ng.ngcommon.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class RecycleBitmap {

	/**
	 * 清理map中的bitmap;
	 * 
	 * @param imgCache
	 *            ImageCacheMap
	 * @param maxSize
	 *            允许添加的最大图片数量
	 * @param freeSize
	 *            释放掉图片的数量;
	 */
	public static void recycleMapCache(LinkedHashMap<String, Bitmap> imgCache,
			int maxSize, int freeSize) {
		// 超出最大容量时清理
		if (imgCache.values().size() > maxSize) {
			synchronized (imgCache) {
				Iterator<String> it = imgCache.keySet().iterator();
				while (it.hasNext() && (imgCache.keySet().size() > freeSize)) {
					Bitmap bmp = imgCache.get(it.next());
					if (bmp != null && !bmp.isRecycled()) {
						bmp.recycle();
						bmp = null;
					}
					it.remove();
				}
			}
			System.gc();
		}
	}

	/**
	 * 清理View中的ImagView被BitMap占用的内存;
	 * 
	 * @param mapViews
	 *            一个View的合集
	 */
	public static void recycle(Map<View, int[]> mapViews) {
		synchronized (mapViews) {
			Iterator<View> it = mapViews.keySet().iterator();
			while (it.hasNext()) {
				// 获取布局
				View view = it.next();
				if (view == null)
					return;
				// 获取要布局内要回收的ids;
				int[] recycleIds = mapViews.get(view);

				// 如果是listView,先找到每个布局文件.重要提示:每个ImagView在布局文件的第一层;
				if ((view instanceof AbsListView)) {
					recycleAbsList((AbsListView) view, recycleIds);
				}
				// 如果是ImageView,直接回收;
				else if (view instanceof ImageView) {
					recycleImageView(view);
				}
				// 如果是ViewGroup,重要提示:每个ImagView在ViewGroup的第二层;
				else if ((view instanceof ViewGroup)) {
					recycleViewGroup((ViewGroup) view, recycleIds);
				}
			}
		}
		System.gc();
	}

	/**
	 * 回收继承自AbsListView的类,如GridView,ListView等
	 * 
	 * @param absView
	 * @param recycleIds
	 *            要清理的Id的集合;
	 */
	public static void recycleAbsList(AbsListView absView, int[] recycleIds) {
		if (absView == null)
			return;
		synchronized (absView) {
			// 回收当前显示的区域
			for (int index = absView.getFirstVisiblePosition(); index <= absView
					.getLastVisiblePosition(); index++) {
				// 获取每一个显示区域的具体ItemView
				ViewGroup views = (ViewGroup) absView.getAdapter().getView(
						index, null, absView);
				for (int count = 0; count < recycleIds.length; count++) {
					recycleImageView(views.findViewById(recycleIds[count]));
				}
			}
		}
	}

	/**
	 * 回收继承自AbsListView的类,如GridView,ListView等
	 * 
	 * @param recycleIds
	 *            要清理的Id的集合;
	 */
	public static void recycleViewGroup(ViewGroup layout, int[] recycleIds) {
		if (layout == null)
			return;
		synchronized (layout) {
			for (int i = 0; i < layout.getChildCount(); i++) {
				View subView = layout.getChildAt(i);
				if (subView instanceof ViewGroup) {
					for (int count = 0; count < recycleIds.length; count++) {
						recycleImageView(subView
								.findViewById(recycleIds[count]));
					}
				} else {
					if (subView instanceof ImageView) {
						recycleImageView((ImageView) subView);
					}
				}
			}
		}
	}

	/**
	 * 回收ImageView占用的图像内存;
	 * 
	 * @param view
	 */
	public static void recycleImageView(View view) {
		if (view == null)
			return;
		if (view instanceof ImageView) {
			Drawable drawable = ((ImageView) view).getDrawable();
			if (drawable instanceof BitmapDrawable) {
				Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
				if (bmp != null && !bmp.isRecycled()) {
					((ImageView) view).setImageBitmap(null);
					bmp.recycle();
					bmp = null;
				}
			}
		}
	}

	public static void destoryBitmap(Bitmap bmp) {
		if (bmp != null) {
			bmp.recycle();
			bmp = null;
		}
	}

	public static void loadLocalDrawable(Context context, View view,
			int drawableid) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(drawableid);
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (view instanceof ImageView) {
			((ImageView) view).setImageBitmap(bitmap);

		} else {
			BitmapDrawable bd = new BitmapDrawable(context.getResources(),
					bitmap);
			view.setBackgroundDrawable(bd);
		}

	}

	public static void loadLocalBigDrawable(Context context, View view,
			int drawableid) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(drawableid);
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (view instanceof ImageView) {
			((ImageView) view).setImageBitmap(bitmap);

		} else {
			BitmapDrawable bd = new BitmapDrawable(context.getResources(),
					bitmap);
			view.setBackgroundDrawable(bd);
		}

	}

	// public static Bitmap readBitMap(Context context, int resId) {
	// BitmapFactory.Options opt = new BitmapFactory.Options();
	// opt.inPreferredConfig = Bitmap.Config.RGB_565;
	// opt.inPurgeable = true;
	// opt.inInputShareable = true;
	// // 获取资源图片
	// InputStream is = context.getResources().openRawResource(resId);
	// return BitmapFactory.decodeStream(is, null, opt);
	// }

}
