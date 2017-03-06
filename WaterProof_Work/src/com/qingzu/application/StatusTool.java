package com.qingzu.application;

/**
 * 状态
 * 
 */
public class StatusTool {
	/**
	 * 选择城市 
	 */
	public static final int SELECT_CITY = 20; // 选择城市
	/**
	 * 老板选择地址 
	 */
	public static final int BOOS_SELECT_PLACE = 21;// 老板选择地址
	/**
	 * 工长选择地址 
	 */
	public static final int CAPTAIN_SELECT_PLACE = 22;// 工长选择地址
	/**
	 * 工人选择地址
	 */
	public static final int WORKER_SELECT_PLACE = 23;// 工人选择地址
	/**
	 * 选择地址
	 */
	public static final int SELECT_PLACE = 24;// 选择地址
	/**
	 * 选择图片
	 */
	public static final int SELECT_PICTURE = 25; // 选择图片
	/**
	 * 是否从Welcome跳转到主页
	 */
	public static boolean isWelcome = false; // 是否从Welcome跳转到主页
	
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	
    public static final int MEMBER_ATTESTATION = 26;//实名认证选择图片
//	public static final int REQUEST_SELECT = 24;// 选择地址
	

}
