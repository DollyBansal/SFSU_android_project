package com.memoir.fragment;

import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;

public class AllListFragment extends HomeListViewAllFragment {

	@Override
	protected String getOrderBy() {
		return MemoirQuery.STARTDATE + " DESC";
	}

	@Override
	protected String stringSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] stringArgument() {
		// TODO Auto-generated method stub
		return null;
	}
}
