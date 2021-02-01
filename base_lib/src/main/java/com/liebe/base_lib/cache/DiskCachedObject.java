package com.liebe.base_lib.cache;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Cache object in Disk.
 */
public class DiskCachedObject implements Serializable {

    private static final long serialVersionUID = 0L;

    @SerializedName("expiry_time_seconds")
    private int mExpiryTimeSeconds;
    @SerializedName("expiry_timestamp")
    private int mExpiryTimestamp;
    @SerializedName("payload")
    private Object mPayload;

    public DiskCachedObject(Object payload, int expiryTimeSeconds) {
        mExpiryTimeSeconds = expiryTimeSeconds <= 0 ? -1 : expiryTimeSeconds;
        int creationTimestamp = (int) (System.currentTimeMillis() / 1000L);
        mExpiryTimestamp = expiryTimeSeconds <= 0 ? -1 : creationTimestamp + mExpiryTimeSeconds;
        mPayload = payload;
    }

    public boolean isExpired() {
        return mExpiryTimeSeconds >= 0 && mExpiryTimestamp < (int) (System.currentTimeMillis() / 1000L);
    }

    public Object getPayload() {
        return mPayload;
    }
}
