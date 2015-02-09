package com.redmadrobot.azoft.collage.loaders;

import android.content.Context;
import com.mig35.loaderlib.loaders.DataAsyncTaskLibLoader;
import com.redmadrobot.azoft.collage.data.CollageRegionData;
import com.redmadrobot.azoft.collage.data.Post;
import com.redmadrobot.azoft.collage.exceptions.DiskWriteException;
import com.redmadrobot.azoft.collage.exceptions.InternalServerException;
import com.redmadrobot.azoft.collage.server.ServerConnector;
import com.redmadrobot.azoft.collage.utils.CollageRegion;
import com.redmadrobot.azoft.collage.utils.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Will load big image for collage item from the Internet.
 * Stores it to external or internal cache dir.
 * <p/>
 * Date: 4/9/2014
 * Time: 12:08 PM
 *
 * @author MiG35
 */
public class CollageBigImageLoader extends DataAsyncTaskLibLoader<CollageRegionData> {

	private static final String IMAGE_NAME = "inst_img_%d.jpg";

	private final CollageRegion mCollageRegion;
	private final Post mPost;

	public CollageBigImageLoader(final Context context, final CollageRegion collageRegion, final Post post) {
		super(context);
		if (null == collageRegion || null == post) {
			throw new IllegalArgumentException("collageRegion and post can't be null");
		}

		mCollageRegion = collageRegion;
		mPost = post;
	}

	@Override
	protected CollageRegionData performLoad() throws Exception {
		final File filesDir = CommonUtils.getCacheFileDir();
		if (null == filesDir) {
			throw new DiskWriteException();
		}
		final File outputFile = new File(filesDir, String.format(IMAGE_NAME, mCollageRegion.getId()));
		if (outputFile.exists() && !outputFile.delete()) {
			throw new DiskWriteException();
		}

		final HttpURLConnection connection = (HttpURLConnection) new URL(mPost.getStandardResolutionImage().getUrl()).openConnection();

		connection.setUseCaches(true);
		final int responseCode = connection.getResponseCode();
		if (responseCode >= 300) {
			connection.disconnect();
			throw new InternalServerException();
		}

		CommonUtils.writeNetworkStreamToAnOtherStream(connection.getInputStream(), new FileOutputStream(outputFile));
		connection.disconnect();

		if (outputFile.exists()) {
			return new CollageRegionData(outputFile);
		}
		else {
			throw new DiskWriteException();
		}
	}
}