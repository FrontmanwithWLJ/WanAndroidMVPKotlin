package cqupt.sl.wanandroidmk.net.service

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface HttpServices {

    @GET
    fun get(@Url url:String, @QueryMap queryMap: Map<String, String>?):@JvmSuppressWildcards Call<ResponseBody>

    @FormUrlEncoded
    @POST
    fun post(@Url url: String,@FieldMap fieldMap: Map<String,String>,
                 @QueryMap queryMap: Map<String, String>):Call<ResponseBody>

    //Body要和FormUrlEncoded分开
    @POST
    fun jsonPost(@Url url: String,@Body body: RequestBody) :Call<ResponseBody>

    @Streaming
    @GET
    fun download(@Url fileUrl : String):Call<ResponseBody>
}