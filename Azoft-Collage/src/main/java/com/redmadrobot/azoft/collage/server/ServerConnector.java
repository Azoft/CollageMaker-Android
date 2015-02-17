package com.redmadrobot.azoft.collage.server;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mig35.loaderlib.exceptions.NoNetworkException;
import com.redmadrobot.azoft.collage.exceptions.InternalServerException;
import com.squareup.okhttp.OkHttpClient;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

import java.util.concurrent.TimeUnit;

/**
 * Contains all service components such as Instgram and general Http Client. Thread safe singleton.
 * <p/>
 * Date: 6/28/13
 * Time: 7:24 PM
 *
 * @author MiG35
 */
public final class ServerConnector {

	private static final String SERVER_URL = "https://api.instagram.com/";
	private static final String VERSION = "v1/";
	private static final String SERVER_URL_WITH_VERSION = SERVER_URL + VERSION;

	public static final String CLIENT_ID = "09eb51d4d3ab42518831408de280cbaf";
	private static final long TIMEOUT_TIME = 10;

	private final InstagramService mInstagramService;
	private final OkHttpClient mOkHttpClient;

	private ServerConnector() {
		final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

		final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(SERVER_URL_WITH_VERSION).setConverter(new GsonConverter(gson))
				.setErrorHandler(new ErrorHandler() {
					@Override
					public Throwable handleError(final RetrofitError cause) {
						if (cause.isNetworkError()) {
							return new NoNetworkException();
						}
						else {
							return new InternalServerException(cause);
						}
					}
				}).build();

		mInstagramService = restAdapter.create(InstagramService.class);

		mOkHttpClient = new OkHttpClient();
		mOkHttpClient.setConnectTimeout(TIMEOUT_TIME, TimeUnit.SECONDS);
		mOkHttpClient.setReadTimeout(TIMEOUT_TIME, TimeUnit.SECONDS);
	}

	public static InstagramService getInstagramService() {
		return Holder.SERVER_CONNECTOR.mInstagramService;
	}

	public static OkHttpClient getOkHttpClient() {
		return Holder.SERVER_CONNECTOR.mOkHttpClient;
	}

	private static final class Holder {

		private static final ServerConnector SERVER_CONNECTOR = new ServerConnector();
	}
}