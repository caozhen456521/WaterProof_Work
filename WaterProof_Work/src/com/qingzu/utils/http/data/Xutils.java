package com.qingzu.utils.http.data;

import java.lang.reflect.Type;
import java.util.Map;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import com.google.gson.Gson;

public class Xutils {
	/**
	 * 发送get请求
	 * 
	 * @param <T>
	 */
	public static <T> Cancelable Get(RequestParams params,
			CommonCallback<T> callback) {
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}

	/**
	 * 发送post请求
	 * 
	 * @param <T>
	 */
	public static <T> Cancelable Post(RequestParams params,
			CommonCallback<T> callback) {
		Cancelable cancelable = x.http().post(params, callback);
		return cancelable;
	}

	/**
	 * 发送put请求
	 * 
	 * @param <T>
	 */
	public static <T> Cancelable Put(RequestParams params,CommonCallback<T> callback) {
		Cancelable cancelable = x.http().request(HttpMethod.PUT, params,callback);		
		return cancelable;
	}

	/**
	 * 发送delete请求
	 * 
	 * @param url
	 * @param params
	 * @param callback
	 * @return
	 * @author Johnson
	 */
	public static <T> Cancelable Delete(RequestParams params,
			CommonCallback<T> callback) {
		Cancelable cancelable = x.http().request(HttpMethod.DELETE, params,
				callback);
		return cancelable;
	}

	/**
	 * 上传文件
	 * 
	 * @param <T>
	 */
	public static <T> Cancelable UpLoadFile(String url,
			Map<String, Object> map, CommonCallback<T> callback) {
		RequestParams params = new RequestParams(url);
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		params.setMultipart(true);
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}

	/**
	 * 下载文件
	 * 
	 * @param <T>
	 */
	public static <T> Cancelable DownLoadFile(String url, String filepath,
			CommonCallback<T> callback) {
		RequestParams params = new RequestParams(url);
		// 设置断点续传
		params.setAutoResume(true);
		params.setSaveFilePath(filepath);
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}
}

// class JsonResponseParser implements ResponseParser {
// // 检查服务器返回的响应头信息
// @Override
// public void checkResponse(UriRequest request) throws Throwable {
// }
//
// /**
// * 转换result为resultType类型的对象
// *
// * @param resultType
// * 返回值类型(可能带有泛型信息)
// * @param resultClass
// * 返回值类型
// * @param result
// * 字符串数据
// * @return
// * @throws Throwable
// */
// public Object parse(Type resultType, Class<?> resultClass, String result)
// throws Throwable {
// return new Gson().fromJson(result, resultClass);
// }
//
// }
//
// class MyCallBack<ResultType> implements Callback.CommonCallback<ResultType> {
//
// @Override
// public void onSuccess(ResultType result) {
// // 可以根据公司的需求进行统一的请求成功的逻辑处理
// }
//
// @Override
// public void onError(Throwable ex, boolean isOnCallback) {
// // 可以根据公司的需求进行统一的请求网络失败的逻辑处理
// }
//
// @Override
// public void onCancelled(CancelledException cex) {
//
// }
//
// @Override
// public void onFinished() {
//
// }
//
// }