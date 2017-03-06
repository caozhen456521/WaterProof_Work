package com.qingzu.entity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qingzu.application.AppManager;

public class InterfaceReturn<T> {

	private static Gson gson = null;

	private List<T> results;
	private Integer totalCount;
	private Integer resultsCount;
	private String timestamp;
	private boolean status;
	private String message;

	public static Gson getGson() {
		if (gson == null) {
			gson = new Gson();
		}
		return gson;
	}

	public static InterfaceReturn fromJson(String json, Class clazz) {
		Gson gson = new GsonBuilder().serializeNulls().create();
		Type objectType = type(InterfaceReturn.class, clazz);
		return gson.fromJson(json, objectType);
	}

	public static InterfaceReturn fromJsonModel(String json) {
		Gson gson = new GsonBuilder().serializeNulls().create();
		return gson.fromJson(json, InterfaceReturn.class);
	}

	public String toJson(Class<T> clazz) {
		Gson gson = new Gson();
		Type objectType = type(InterfaceReturn.class, clazz);
		return gson.toJson(this, objectType);
	}

	static ParameterizedType type(final Class raw, final Type... args) {
		return new ParameterizedType() {
			public Type getRawType() {
				return raw;
			}

			public Type[] getActualTypeArguments() {
				return args;
			}

			public Type getOwnerType() {
				return null;
			}
		};
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getResultsCount() {
		return resultsCount;
	}

	public void setResultsCount(Integer resultsCount) {
		this.resultsCount = resultsCount;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public InterfaceReturn(List<T> results, Integer totalCount,
			Integer resultsCount, String timestamp, boolean status,
			String message) {
		super();
		this.results = results;
		this.totalCount = totalCount;
		this.resultsCount = resultsCount;
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
	}

	public InterfaceReturn() {
		super();
		// TODO Auto-generated constructor stub
	}

}
