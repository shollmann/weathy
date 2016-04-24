package com.shollmann.weathy.api;

import com.shollmann.weathy.api.baseapi.BaseApi;
import com.shollmann.weathy.api.baseapi.BaseApiCall;
import com.shollmann.weathy.api.baseapi.CachePolicy;
import com.shollmann.weathy.api.baseapi.CallId;
import com.shollmann.weathy.api.contract.OpenWeatherContract;
import com.shollmann.weathy.api.model.WeatherReport;
import com.shollmann.weathy.helper.Constants;

import retrofit.Callback;
import retrofit.RequestInterceptor;

public class OpenWeatherApi extends BaseApi<OpenWeatherContract> {

    public OpenWeatherApi(String baseUrl) {
        super(baseUrl, OpenWeatherContract.class);
    }

    @Override
    protected void onRequest(RequestInterceptor.RequestFacade request) {
        request.addQueryParam(Constants.OpenWeatherApi.APP_ID_QUERY_PARAM, Constants.OpenWeatherApi.API_KEY);
    }

    public void getMarketingBanner(String cityName, CallId callId, Callback<WeatherReport> callback) {
        CachePolicy cachePolicy = CachePolicy.CACHE_ELSE_NETWORK;
        cachePolicy.setCacheKey(String.format("weather_report_for_%1$s", cityName));
        cachePolicy.setCacheTTL(Constants.Time.TEN_MINUTES);

        BaseApiCall<WeatherReport> apiCall = registerCall(callId, cachePolicy, callback, WeatherReport.class);

        if (apiCall != null && apiCall.requiresNetworkCall()) {
            getService().getWeatherForCityName(cityName, apiCall);
        }
    }

}
