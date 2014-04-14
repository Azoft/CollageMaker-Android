package com.redmadrobot.azoft.collage.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.mig35.injectorlib.utils.inject.InjectSavedState;
import com.mig35.injectorlib.utils.inject.InjectView;
import com.redmadrobot.azoft.collage.R;
import com.redmadrobot.azoft.collage.data.Collage;
import com.redmadrobot.azoft.collage.ui.adapters.CollageAdapter;
import com.redmadrobot.azoft.collage.utils.collagegenerators.CollageFactory;
import com.redmadrobot.azoft.collage.utils.collagegenerators.SimpleCollageGenerator;

/**
 * Date: 4/8/2014
 * Time: 4:05 PM
 *
 * @author MiG35
 */
public class CollageSelectionFragment extends ActionBarLoaderFragment {

	private static final CollageFactory COLLAGE_FACTORY = new SimpleCollageGenerator();

	@InjectSavedState
	private Integer mSelectedCollage;

	@InjectView(R.id.gv_collagees)
	private GridView mCollageGridView;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_collage_selection, container, false);
	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final CollageAdapter collageAdapter = new CollageAdapter(getActivity(), COLLAGE_FACTORY, mSelectedCollage);
		mCollageGridView.setAdapter(collageAdapter);
		mCollageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				collageAdapter.setSelected(position);
				mSelectedCollage = position;
			}
		});
	}

	public Collage getSelectedCollage() {
		if (null == mSelectedCollage) {
			return null;
		}
		return COLLAGE_FACTORY.getCollage(mSelectedCollage);
	}
}