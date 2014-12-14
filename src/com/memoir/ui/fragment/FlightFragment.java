package com.memoir.ui.fragment;

import com.memoir.R;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;
import com.memoir.model.Memoir.Memoirs;

public class FlightFragment extends HomeListViewAllFragment {

	@Override
	protected String stringSelection() {
		// TODO Auto-generated method stub
		return Memoirs.BY_Type;
	}

	@Override
	protected String[] stringArgument() {
		// TODO Auto-generated method stub
		String str = getResources().getString(R.string.flight);
		return new String[] { "FLIGHT" };
	}

	@Override
	protected String getOrderBy() {
		// TODO Auto-generated method stub
		return MemoirQuery.STARTDATE + " DESC";
	}

}
