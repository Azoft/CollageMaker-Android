package com.azoft.azoft.collage.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

import com.mig35.loaderlib.exceptions.NoNetworkException;
import com.azoft.azoft.collage.R;
import com.azoft.azoft.collage.app.CollageApplication;
import com.azoft.azoft.collage.exceptions.DiskWriteException;
import com.azoft.azoft.collage.exceptions.InternalServerException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public final class CommonUtils {

    public static File getCacheFileDir() {
        File rootCacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            rootCacheDir = CollageApplication.getInstance().getExternalCacheDir();
        }
        if (checkAndCreateDirIfNotExists(rootCacheDir)) {
            return rootCacheDir;
        }
        rootCacheDir = getInternalCacheDir();
        if (checkAndCreateDirIfNotExists(rootCacheDir)) {
            return rootCacheDir;
        }

        return null;
    }

    public static File getInternalCacheDir() {
        final File rootCacheDir = CollageApplication.getInstance().getCacheDir();

        if (checkAndCreateDirIfNotExists(rootCacheDir)) {
            return rootCacheDir;
        }

        return null;
    }

    public static File getInternalFilesDir() {
        final File rootFilesDir = CollageApplication.getInstance().getFilesDir();

        if (checkAndCreateDirIfNotExists(rootFilesDir)) {
            return rootFilesDir;
        }

        return null;
    }

    public static boolean checkAndCreateDirIfNotExists(final File rootCacheDir) {
        if (null != rootCacheDir) {
            if (!rootCacheDir.exists() && rootCacheDir.mkdirs()) {
                return true;
            }

            if (rootCacheDir.exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Will search connected or connecting network. If found do nothing, if not,
     * throws exception
     *
     * @throws java.io.IOException if no network found
     */
    public static void checkInternet() throws IOException {
        final ConnectivityManager cm = (ConnectivityManager) CollageApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            throw new NoNetworkException();
        }
    }

    public static void writeNetworkStreamToAnOtherStream(final InputStream inputStream, final OutputStream outputStream)
            throws DiskWriteException, InternalServerException, IOException {
        if (null == inputStream || null == outputStream) {
            throw new IllegalArgumentException("input and output streams can't be null");
        }

        boolean read = true;
        try {
            try {
                final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                final byte[] buffer = new byte[2048];
                int readCount;
                while ((readCount = bufferedInputStream.read(buffer)) != -1) {
                    read = false;
                    bufferedOutputStream.write(buffer, 0, readCount);
                    read = true;
                }
                bufferedOutputStream.flush();
            } finally {
                inputStream.close();
            }
        } catch (final IOException e) {
            if (read) {
                checkInternet();
                throw new InternalServerException(e);
            } else {
                throw new DiskWriteException(e);
            }
        } finally {
            outputStream.close();
        }
    }

    private CommonUtils() {
    }

    @SuppressWarnings("ConstantConditions")
    public static void sendEmailWithAttachment(final Context context, final String path, final String emailSubject) {
        final Uri uriAsPath = Uri.parse(path);
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uriAsPath);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailSubject);
        emailIntent.setType("image/*");
        final List<ResolveInfo> emailActivities = context.getPackageManager().queryIntentActivities(emailIntent, 0);
        // we should grand all interested apps read permission
        for (final ResolveInfo resolveInfo : emailActivities) {
            if (null != resolveInfo.activityInfo) {
                context.grantUriPermission(resolveInfo.activityInfo.packageName, uriAsPath, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }

        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.text_email_chooser)));
    }
}