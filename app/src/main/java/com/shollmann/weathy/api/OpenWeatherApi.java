package com.shollmann.weathy.api;

import com.shollmann.weathy.api.baseapi.BaseApi;
import com.shollmann.weathy.api.contract.OpenWeatherContract;

public class OpenWeatherApi extends BaseApi<OpenWeatherContract> {

    private String baseUrl;
    private int environment;

    public OpenWeatherApi(String baseUrl, int environment) {
        super(baseUrl, OpenWeatherContract.class);
        this.baseUrl = baseUrl;
        this.environment = environment;
    }

    public void setEnvironment(int environment) {
        this.environment = environment;
    }

//    @Override
//    protected void onRequest(RequestInterceptor.RequestFacade request) {
//        request.addQueryParam("appid", Constants.OPEN_WEATHER_MAP_API_KEY);
//    }


//    public void getSuggestionFor(String word, CallId callId, Callback<Autosuggestion> callback) {
//        String mostAccurateLocationUrl = LocationHelper.getCurrentStateOrCountry();
//
//        CachePolicy cachePolicy = CachePolicy.CACHE_ELSE_NETWORK;
//        cachePolicy.setCacheKey(String.format("autocomplete_for_%1$s_in_%2$s", word, mostAccurateLocationUrl));
//        cachePolicy.setCacheTTL(Constants.Time.TWO_DAYS);
//
//        BaseApiCall<Autosuggestion> apiCall = registerCall(callId, cachePolicy, callback, Autosuggestion.class);
//        if (apiCall != null && apiCall.requiresNetworkCall()) {
//            getService().getSuggestionFor(mostAccurateLocationUrl, word, apiCall);
//        }
//    }
//
//    public void getMarketingBanner(String countryUrl, String layout, CallId callId, Callback<BannerResponse> callback) {
//        CachePolicy cachePolicy = CachePolicy.CACHE_ELSE_NETWORK;
//        cachePolicy.setCacheKey(String.format("marketingBanner_for_%1$s_in_%2$s", countryUrl, layout));
//        cachePolicy.setCacheTTL(Constants.Time.ONE_HOUR);
//
//        BaseApiCall<BannerResponse> apiCall = registerCall(callId, cachePolicy, callback, BannerResponse.class);
//
//        if (apiCall != null && apiCall.requiresNetworkCall()) {
//            getService().getMarketingBanner(countryUrl, layout, apiCall);
//        }
//    }


}
