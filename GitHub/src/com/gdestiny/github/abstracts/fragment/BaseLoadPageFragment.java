package com.gdestiny.github.abstracts.fragment;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.client.PageIterator;

import com.gdestiny.github.ui.view.MoreListView;
import com.gdestiny.github.ui.view.MoreListView.OnAutoLoadListener;
import com.gdestiny.github.utils.CacheUtils;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.client.IteratorUtils;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;

public abstract class BaseLoadPageFragment<Elem, Params> extends
		BaseLoadFragment<Params, List<Elem>> implements OnItemClickListener,
		OnItemLongClickListener {

	private MoreListView moreList;
	private BaseAdapter baseAdapter;

	private List<Elem> datas;

	private PageIterator<Elem> dataPage;
	private boolean refresh = true;

	public void setContentView(LayoutInflater inflater, int id, int refreshId,
			int moreListId) {
		setContentView(inflater, id, refreshId);
		setMoreListView(moreListId);
	}

	public void setMoreListView(int id) {
		moreList = (MoreListView) findViewById(id);

		newListAdapter();

		moreList.setOnFooterClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				execute(null);
			}
		});
		moreList.setOnItemClickListener(this);
		moreList.setOnItemLongClickListener(this);
		moreList.setOnAutoLoadListener(new OnAutoLoadListener() {

			@Override
			public void onAutoLoad() {
				execute(null);
			}
		});
	}

	@Override
	public List<Elem> onBackground(Params params) throws Exception {
		if (refresh) {
			newPageData(params);
		}
		List<Elem> list = IteratorUtils.iteratorNextPage(dataPage);
		if (refresh && !TextUtils.isEmpty(getCacheName())) {
			if (TextUtils.isEmpty(getSubDir()))
				CacheUtils.cacheObject(getCacheName(), list);
			else
				CacheUtils.cacheObject(getSubDir(), getCacheName(), list);
		}
		return list;
	}

	public abstract void newListAdapter();

	/**
	 * new完调用setDataPage
	 */
	public abstract void newPageData(Params params);

	@Override
	public void onSuccess(List<Elem> result) {
		super.onSuccess(result);
		if (refresh) {
			datas.clear();
			refresh = false;
		}
		datas.addAll(result);
		baseAdapter.notifyDataSetChanged();
		moreList.requestLoadingFinish();
		moreList.requestNoMore(result.size() < Constants.DEFAULT_PAGE_SIZE
				|| !dataPage.hasNext());
		noData(datas == null || datas.size() == 0);
	}

	@Override
	public void onException(Exception ex) {
		super.onException(ex);
		moreList.requestLoadingFinish();
		if (datas == null || datas.size() == 0) {
			moreList.requestNoMore(true);
			noData(true);
		}
	}

	@Override
	public void onRefresh() {
		if (datas == null)
			datas = new ArrayList<Elem>();
		// datas.clear();
		dataPage = null;// 赋空值更新
		refresh = true;
	}

	public BaseAdapter getBaseAdapter() {
		return baseAdapter;
	}

	public void setBaseAdapter(BaseAdapter baseAdapter) {
		this.baseAdapter = baseAdapter;
		this.moreList.setAdapter(baseAdapter);
	}

	public List<Elem> getDatas() {
		if (datas == null)
			datas = new ArrayList<Elem>();
		return datas;
	}

	public void setDatas(List<Elem> datas) {
		if (datas == null)
			return;
		this.datas = datas;
	}

	public PageIterator<Elem> getDataPage() {
		return dataPage;
	}

	public void setDataPage(PageIterator<Elem> dataPage) {
		this.dataPage = dataPage;
	}

	public MoreListView getMoreList() {
		return moreList;
	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

}
