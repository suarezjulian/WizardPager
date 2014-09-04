package co.juliansuarez.libwizardpager.wizard.model;

import android.support.v4.app.Fragment;
import co.juliansuarez.libwizardpager.wizard.ui.ImageFragment;

public class ImagePage extends TextPage {

	public ImagePage(ModelCallbacks callbacks, String title) {
		super(callbacks, title);
	}

	@Override
	public Fragment createFragment() {
		return ImageFragment.create(getKey());
	}

	public ImagePage setValue(String value) {
		mData.putString(SIMPLE_DATA_KEY, value);
		return this;
	}
}
