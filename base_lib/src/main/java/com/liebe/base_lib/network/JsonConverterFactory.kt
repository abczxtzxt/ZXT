package com.liebe.base_lib.network

import com.liebe.base_lib.util.AesUtil
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.StringReader
import java.lang.reflect.Type


/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 自定义ConverterFactory，进行加密解密操作
 * @Author LiangZe
 * @Date 2020/4/20
 * @Version 2.0
 */

class JsonConverterFactory constructor(var gson: Gson): Converter.Factory() {

    //响应
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return JsonResponseBodyConverter(gson,adapter)
    }

//    //请求
//    override fun requestBodyConverter(
//        type: Type,
//        parameterAnnotations: Array<Annotation>,
//        methodAnnotations: Array<Annotation>,
//        retrofit: Retrofit
//    ): Converter<*, RequestBody>? {
//        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
//        return JsonRequestBodyConverter(gson,adapter)
//    }
//
//    class JsonRequestBodyConverter<T>  constructor(var gson: Gson , var adapter: TypeAdapter<T>): Converter<T,RequestBody>{
//        override fun convert(value: T): RequestBody? {
//            //对生成的json 进行加密
//            Flog.e("JsonConverterFactory",value.toString())
//            return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),value.toString())
//        }
//    }

    class JsonResponseBodyConverter<T> constructor(var gson: Gson , var adapter: TypeAdapter<T>) : Converter<ResponseBody ,T>{
        override fun convert(value: ResponseBody): T? {
            val jsonString=AesUtil().decrypt(value.string())
            val reader=StringReader(jsonString)
            val jsonReader=gson.newJsonReader(reader)
            return adapter.read(jsonReader)
        }
    }

}