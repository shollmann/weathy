package com.shollmann.weathy.api.baseapi;

import com.shollmann.weathy.db.CachingDbHelper;
import com.shollmann.weathy.db.DbItem;
import com.shollmann.weathy.ui.WeathyApplication;

import java.io.Serializable;
import java.lang.reflect.Type;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BaseApiCall<T> implements Callback<T> {

    private CachingDbHelper cachingDb;
    BaseApi baseApi;
    private CallId callId;
    private CachePolicy cachePolicy;
    private Callback<T> callback;
    Type responseType;

    private boolean isCancelled = false;

    private T pendingResult = null;
    private Response pendingResponse = null;
    private RetrofitError pendingError = null;

    public BaseApiCall(BaseApi baseApi, CallId callId, CachePolicy cachePolicy, Callback<T> callback, Type responseType) {
        this.cachingDb = WeathyApplication.getApplication().getCachingDbHelper();
        this.baseApi = baseApi;
        this.callId = callId;
        this.cachePolicy = cachePolicy;
        this.callback = callback;
        this.responseType = responseType;
    }

    public boolean requiresNetworkCall() {
        if (cachePolicy == CachePolicy.NETWORK_ONLY || cachePolicy == CachePolicy.NETWORK_ELSE_ANY_CACHE) {
//            //LogIntternal.logBaseApiCall("requires network call", callId.toString());
            return true;
        }
        DbItem<T> cachedResponse = cachingDb.getDbItem(cachePolicy.getCacheKey(), responseType);
        if (cachedResponse == null) {
//            //LogIntternal.logBaseApiCall("requires network call", callId.toString());
            return true;
        }
        if (!cachedResponse.isExpired(cachePolicy.getCacheTTL())) {
//            //LogIntternal.logBaseApiCall("answering valid-cache", callId.toString());
//            //LogIntternal.logBaseApiCall("skipping network call", callId.toString());
            success(cachedResponse.getObject(), null);
            return false;
        } else if (cachePolicy == CachePolicy.ANY_CACHE_THEN_NETWORK) {
//            //LogIntternal.logBaseApiCall("answering expired-cache", callId.toString());
            success(cachedResponse.getObject(), null);
            if (cachePolicy != CachePolicy.ANY_CACHE_THEN_NETWORK) {
//                //LogIntternal.logBaseApiCall("skipping network call", callId.toString());
                return false;
            }
        }
//        //LogIntternal.logBaseApiCall("requires network call", callId.toString());
        return true;
    }

    @Override
    public synchronized void success(T result, Response response) {
        if (response != null) { //response != null means that the result is from net and not from cache
            if (cachePolicy.getCacheKey() != null && result instanceof Serializable) {
                //LogIntternal.logBaseApiCall("updating cache-db", callId.toString(), Log.DEBUG);
                cachingDb.insert(cachePolicy.getCacheKey(), (Serializable) result, cachePolicy.getCacheTTL());
            } else {
                //LogIntternal.logBaseApiCall("skipping cache-db update", callId.toString(), Log.DEBUG);
            }
        }
        if (!isCancelled) {
            if (callback != null) {
                //LogIntternal.logBaseApiCall("success response", callId.toString());
                callback.success(result, response);
                baseApi.removeCall(callId);
            } else {
                //LogIntternal.logBaseApiCall("success on-hold", callId.toString());
                pendingResult = result;
                pendingResponse = response;
            }
        } else {
            //LogIntternal.logBaseApiCall("success ignored", callId.toString(), Log.DEBUG);
        }
    }

    @Override
    public synchronized void failure(RetrofitError error) {
        if (!isCancelled) {
            if (callback != null) {
                if (cachePolicy == CachePolicy.CACHE_ELSE_NETWORK_ELSE_ANY_CACHE || cachePolicy == CachePolicy.NETWORK_ELSE_ANY_CACHE) {
                    DbItem<T> cachedResponse = cachingDb.getDbItem(cachePolicy.getCacheKey(), responseType);
                    if (cachedResponse == null) {
                        //LogIntternal.logBaseApiCall("failure response", callId.toString());
                        callback.failure(error);
                    } else {
                        //LogIntternal.logBaseApiCall("using any-cache", callId.toString());
                        //LogIntternal.logBaseApiCall("success response", callId.toString());
                        callback.success(cachedResponse.getObject(), null);
                    }
                } else {
                    //LogIntternal.logBaseApiCall("failure response", callId.toString());
                    callback.failure(error);
                }
                baseApi.removeCall(callId);
            } else {
                //LogIntternal.logBaseApiCall("failure on-hold", callId.toString());
                pendingError = error;
            }
        } else {
            //LogIntternal.logBaseApiCall("failure ignored", callId.toString(), Log.DEBUG);
        }
    }

    public synchronized void cancelCall() {
        removeCallback();
        isCancelled = true;
        baseApi.removeCall(callId);
    }

    public synchronized void removeCallback() {
        this.callback = null;
    }

    public synchronized void updateCallback(Callback<T> callback) {
        this.callback = callback;
        if (!isCancelled && callback != null) {
            if (pendingResponse != null) {
                //LogIntternal.logBaseApiCall("success delivered", callId.toString());
                success(pendingResult, pendingResponse);
            } else if (pendingError != null) {
                //LogIntternal.logBaseApiCall("failure delivered", callId.toString());
                failure(pendingError);
            }
        }
    }

}
