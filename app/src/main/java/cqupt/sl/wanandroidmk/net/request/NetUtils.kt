package cqupt.sl.wanandroidmk.net.request

import com.google.gson.Gson
import cqupt.sl.wanandroidmk.net.callback.NetCallBack
import cqupt.sl.wanandroidmk.net.service.HttpServices
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Frontman
 * @date 2019-12-12
 * 网络工具类 get post download
 */
object NetUtils {

    private lateinit var baseUrl: String
    private var successCode: Int = 0
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * 设置基础url
     * @param url 基础url
     */
    fun baseUrl(url: String): NetUtils {
        baseUrl = url
        return this
    }

    /**
     * @param code 设置请求成功之后，response里面的code成功值
     */
    fun successCode(code: Int): NetUtils {
        successCode = code
        return this
    }

    /**
     * @param url 可以是完整url、或者基础url的额外部分
     * @param queryMap 接口地址？后面的额外参数
     * @param netCallBack 返回bean对象的接口实例
     * @param clazz 指定bean的类型
     */
    fun <T> get(
        url: String,
        queryMap: Map<String, String>,
        netCallBack: NetCallBack<T>,
        clazz: Class<T>
    ) {
        val service = retrofit.create(HttpServices::class.java)
        val call: Call<ResponseBody> = service.get(url, queryMap)
        call.enqueue(
            object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    netCallBack.onFailure(null)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val gson = Gson()
                    if (response.isSuccessful) {
                        val json: String = response.body().string()
                        netCallBack.onSuccess(gson.fromJson(json, clazz))
                        return
                    }
                    netCallBack.onFailure(response.errorBody())
                }

            }
        )
    }

    /**
     * @param url 可以是完整url、或者基础url的额外部分
     * @param fieldMap 额外参数，不会显示在接口上
     * @param queryMap 接口地址？后面的额外参数
     * @param netCallBack 返回bean对象的接口实例
     * @param clazz 指定bean的类型
     */
    fun <T> post(
        url: String,
        fieldMap: Map<String, String>,
        queryMap: Map<String, String>,
        netCallBack: NetCallBack<T>,
        clazz: Class<T>
    ) {
        val service = retrofit.create(HttpServices::class.java)
        var call = service.post(url, fieldMap, queryMap)
        call.enqueue(
            object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    netCallBack.onFailure(null)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val gson = Gson()
                        val json = response.body().string()
                        netCallBack.onSuccess(gson.fromJson(json, clazz))
                        return
                    }
                    netCallBack.onFailure(response.errorBody())
                }
            }
        )
    }

    /**
     * @param url 可以是完整url、或者基础url的额外部分
     * @param body 额外参数，不会显示在接口上,以json形式发送
     * @param netCallBack 返回bean对象的接口实例
     * @param clazz 指定bean的类型
     */
    fun <T> post(
        url: String,
        body: RequestBody,
        netCallBack: NetCallBack<T>,
        clazz: Class<T>
    ) {
        val service = retrofit.create(HttpServices::class.java)
        var call = service.jsonPost(url, body)
        call.enqueue(
            object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    netCallBack.onFailure(null)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val gson = Gson()
                        val json = response.body().string()
                        netCallBack.onSuccess(gson.fromJson(json, clazz))
                        return
                    }
                    netCallBack.onFailure(response.errorBody())
                }
            }
        )
    }

    /**
     * @param fileUrl 文件下载地址
     * @param clazz 指定bean的类型
     */
    fun <T> download(fileUrl: String, clazz: Class<T>) {

    }
}