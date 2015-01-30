package com.gdestiny.github.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.client.PageIterator;

import com.gdestiny.github.ui.view.MoreListView;
import com.gdestiny.github.ui.view.MoreListView.OnAutoLoadListener;
import com.gdestiny.github.utils.Constants;
import com.gdestiny.github.utils.IteratorUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;

public abstract class BaseLoadPageFragment<Elem, Params> extends
		BaseLoadFragment<Params, List<Elem>> implements OnItemClickListener {

	private MoreListView moreList;
	private BaseAdapter baseAdapter;

	private List<Elem> datas = new ArrayList<Elem>();

	private PageIterator<Elem> dataPage;

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
		moreList.setOnAutoLoadListener(new OnAutoLoadListener() {

			@Override
			public void onAutoLoad() {
				execute(null);
			}
		});
	}

	@Override
	public List<Elem> onBackground(Params params) throws Exception {
		if (dataPage == null)
			newPageData(params);
		return IteratorUtils.iteratorNextPage(dataPage);
	}

	public abstract void newListAdapter();

	/**
	 * new完调用setDataPage
	 */
	public abstract void newPageData(Params params);

	@Override
	public void onSuccess(List<Elem> result) {
		super.onSuccess(result);
		datas.addAll(result);
		baseAdapter.notifyDataSetChanged();
		moreList.requestLoadingFinish();
		moreList.requestNoMore(result.size() < Constants.DEFAULT_PAGE_SIZE
				|| !dataPage.hasNext());
	}

	@Override
	public void onException(Exception ex) {
		super.onException(ex);
		moreList.requestLoadingFinish();
		moreList.requestNoMore(true);
	}

	@Override
	public void onRefreshStarted(View view) {
		if (datas == null)
			datas = new ArrayList<Elem>();
		datas.clear();
		dataPage = null;// 赋空值更新
	}

	public BaseAdapter getBaseAdapter() {
		return baseAdapter;
	}

	public void setBaseAdapter(BaseAdapter baseAdapter) {
		this.baseAdapter = baseAdapter;
		this.moreList.setAdapter(baseAdapter);
	}

	public List<Elem> getDatas() {
		return datas;
	}

	public void setDatas(List<Elem> datas) {
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

}
