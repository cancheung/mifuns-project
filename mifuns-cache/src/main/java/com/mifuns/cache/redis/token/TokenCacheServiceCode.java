package com.mifuns.cache.redis.token;

/**
 * Created by miguangying on 2016/12/22.
 */
public enum TokenCacheServiceCode {
    APP("APP");
    String serviceCode;

    TokenCacheServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    @Override
    public String toString() {
        return this.serviceCode;
    }

    public static TokenCacheServiceCode getServiceCode(String serviceCodeStr){
        if(serviceCodeStr == null || serviceCodeStr.length() == 0){
            return TokenCacheServiceCode.APP;
        }
        for(TokenCacheServiceCode sc : TokenCacheServiceCode.values()){
            if(serviceCodeStr.equals(sc.toString())){
                return sc;
            }
        }
        return TokenCacheServiceCode.APP;
    }

}
