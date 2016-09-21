package com.eip.roucou_c.spred.Errors;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import com.eip.roucou_c.spred.Errors.ApiError;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

/**
 * Created by roucou_c on 14/09/2016.
 */
public class ErrorUtils {

//    public static ApiError parseError(Response<?> response) {
//        Converter<ResponseBody, ApiError> converter = responseBodyConverter(ApiError.class, new Annotation[0]);
//
//        ApiError error;
//
//        try {
//            error = converter.convert(response.errorBody());
//        } catch (IOException e) {
//            return new ApiError();
//        }
//
//        return error;
//    }
}