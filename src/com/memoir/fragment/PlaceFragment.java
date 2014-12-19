package com.memoir.fragment;

import com.memoir.R;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;
import com.memoir.model.Memoir.Memoirs;

public class PlaceFragment extends HomeListViewAllFragment {

	@Override
	protected String stringSelection() {
		// TODO Auto-generated method stub
		return Memoirs.BY_Type;
	}

	@Override
	protected String[] stringArgument() {
		// TODO Auto-generated method stub
		String str = getResources().getString(R.string.place);
		return new String[] { str };
	}

	@Override
	protected String getOrderBy() {
		// TODO Auto-generated method stub
		return MemoirQuery.STARTDATE + " DESC";
	}

}
