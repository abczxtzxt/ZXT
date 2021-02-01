package com.liebe.base_lib.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Cache the data on the disk.
 */
public class DiskCache {
    private static final String HASH_ALGORITHM = "MD5";
    private static final String STRING_ENCODING = "UTF-8";
    private static final String DEFAULT_CACHE_NAME = "data-cache";
    private static final int DEFAULT_CACHE_SIZE = 1024 * 1024 * 10;
    private static final int VALUE_COUNT = 1;

    private DiskLruCache mDiskLruCache;
    private File mCacheDir;
    private int mCacheSize;
    private int mAppVersion;

    public DiskCache(Context context) {
        File cache = context.getExternalCacheDir();
        if (cache == null) {
            cache = context.getCacheDir();
        }
        mCacheDir = new File(cache, DEFAULT_CACHE_NAME);
        mCacheSize = DEFAULT_CACHE_SIZE;
        mAppVersion = getAppVersion(context);
        open();
    }

    public DiskCache(Context context, File cacheDir, int cacheSize) {
        mCacheDir = cacheDir;
        mCacheSize = cacheSize;
        mAppVersion = getAppVersion(context);
        open();
    }

    private void open() {
        try {
            mDiskLruCache = DiskLruCache.open(mCacheDir, mAppVersion, VALUE_COUNT, mCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key) throws IOException {
        String value = null;
        DiskLruCache.Snapshot snapshot = null;

        try {
            snapshot = mDiskLruCache.get(getHashOf(key));
            if (snapshot == null) {
                return null;
            }

            value = snapshot.getString(0);
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        return value;
    }

    public boolean contains(String key) throws IOException {
        boolean found = false;
        DiskLruCache.Snapshot snapshot = null;

        try {
            snapshot = mDiskLruCache.get(getHashOf(key));
            if (snapshot != null) {
                found = true;
            }
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        return found;
    }

    public void setKeyValue(String key, String value) throws IOException {
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(getHashOf(key));
            if (editor == null) {
                return;
            }

            if (writeValueToCache(value, editor)) {
                mDiskLruCache.flush();
                editor.commit();
            } else {
                editor.abort();
            }
        } catch (IOException e) {
            if (editor != null) {
                editor.abort();
            }

            throw e;
        }
    }

    public void clearCache() throws IOException {
        mDiskLruCache.delete();
        // Open the cache again to use the new empty cache
        open();
    }

    private boolean writeValueToCache(String value, DiskLruCache.Editor editor) throws
            IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(editor.newOutputStream(0));
            outputStream.write((value == null) ? "".getBytes(STRING_ENCODING) : value.getBytes(STRING_ENCODING));
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

        return true;
    }

    private String getHashOf(String string) throws UnsupportedEncodingException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            messageDigest.update(string.getBytes(STRING_ENCODING));
            byte[] digest = messageDigest.digest();
            BigInteger bigInt = new BigInteger(1, digest);

            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            return string;
        }
    }

    private int getAppVersion(Context context) {
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
