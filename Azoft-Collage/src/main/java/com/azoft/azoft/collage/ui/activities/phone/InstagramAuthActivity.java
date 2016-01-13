package com.azoft.azoft.collage.ui.activities.phone;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.azoft.azoft.collage.R;
import com.mig35.injectorlib.utils.inject.InjectSavedState;
import com.mig35.injectorlib.utils.inject.InjectView;

public class InstagramAuthActivity extends CollageActivity {

    private static final String REDIRECT_URL = "http://www.azoft.com/";
    private static final String CLIENT_ID = "2679711ad9b14dd3afa52c5c35acb5cd";
    private static final String ACCESS_TOKEN_URL_PATH = "#access_token=";

    private static final String START_URL = "https://api.instagram.com/oauth/authorize/?client_id=%1$s&redirect_uri=%2$s&response_type=token&scope=public_content";

    @InjectView(R.id.web_view)
    private WebView mWebView;
    @InjectView(R.id.progress_bar)
    private View mProgressView;

    @InjectSavedState
    private Bundle mWebViewState;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_instagram_auth);

        initWebView(null == savedInstanceState);

        mWebView.setWebViewClient(new AuthWebClient());

        if (null == mWebViewState) {
            mWebView.loadUrl(String.format(START_URL, CLIENT_ID, REDIRECT_URL));
        } else {
            mWebView.restoreState(mWebViewState);
        }
    }

    private void onTokenGranted(final String accessToken) {
        startActivity(CollageAuthActivity.createIntent(this, StartActivity.class, accessToken));
    }

    private void onTokenFailed() {
        showToast(R.string.error_no_access_token_instagram);
        initWebView(true);
        mWebView.loadUrl(String.format(START_URL, CLIENT_ID, REDIRECT_URL));
    }

    private void initWebView(final boolean clearSetup) {
        final WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setSaveFormData(false);

        if (clearSetup) {
            mWebView.clearCache(true);
            mWebView.clearHistory();
            clearCookies();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        mWebViewState = new Bundle();
        mWebView.saveState(mWebViewState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("deprecation")
    public void clearCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            final CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
            cookieSyncManager.startSync();
            final CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncManager.stopSync();
            cookieSyncManager.sync();
        }
    }

    private class AuthWebClient extends WebViewClient {

        @Override
        public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressView.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            if (url.startsWith(REDIRECT_URL)) {
                final int accessTokenIndex = url.indexOf(ACCESS_TOKEN_URL_PATH);
                if (0 < accessTokenIndex) {
                    final String accessToken = url.substring(accessTokenIndex + ACCESS_TOKEN_URL_PATH.length());
                    onTokenGranted(accessToken);
                } else {
                    onTokenFailed();
                }

                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {
            super.onPageFinished(view, url);
            mProgressView.setVisibility(View.GONE);
        }
    }
}