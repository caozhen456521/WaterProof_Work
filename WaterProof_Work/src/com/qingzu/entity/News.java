package com.qingzu.entity;

import java.io.Serializable;

/**
 * 新闻
 * 
 */
public class News implements Serializable {
	private int id; // 新闻编号
	private int operatorId; // 发布人编号
	private int classId; // 新闻类别
	private String newsTitle; // 新闻标题
	private String subTitle; // 副标题
	private String newsPhoto; // 新闻图片
	private String keywords;// 关键字
	private String description;// 摘要
	private String newsInfo;// 新闻内容
	private String author;// 作者
	private String sources;// 来源
	private String createTime;// 发布时间
	private String updateTime;// 修改时间
	private int clicks;// 点击数
	private boolean isPhotoNews;// 是否图片新闻(0否，1是)
	private boolean isTalk;// 是否允许评论(0否，1是)
	private int isEnable;// 审核状态(0待审核，1已审核，-1已删除)
	private boolean isLinkNews;// 是否转向连接(0否，1是)
    private String linkUrl;//转向地址
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	private int sortValue;// 排序（越大越靠前）

	public News() {
		super();
		// TODO Auto-generated constructor stub
	}

	public News(int id, int operatorId, int classId, String newsTitle,
			String subTitle, String newsPhoto, String keywords,
			String description, String newsInfo, String author, String sources,
			String createTime, String updateTime, int clicks,
			boolean isPhotoNews, boolean isTalk, int isEnable,
			boolean isLinkNews, String linkUrl, int sortValue) {
		super();
		this.id = id;
		this.operatorId = operatorId;
		this.classId = classId;
		this.newsTitle = newsTitle;
		this.subTitle = subTitle;
		this.newsPhoto = newsPhoto;
		this.keywords = keywords;
		this.description = description;
		this.newsInfo = newsInfo;
		this.author = author;
		this.sources = sources;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.clicks = clicks;
		this.isPhotoNews = isPhotoNews;
		this.isTalk = isTalk;
		this.isEnable = isEnable;
		this.isLinkNews = isLinkNews;
	
		this.sortValue = sortValue;
	}

	public int getSortValue() {
		return sortValue;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getNewsPhoto() {
		return newsPhoto;
	}

	public void setNewsPhoto(String newsPhoto) {
		this.newsPhoto = newsPhoto;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNewsInfo() {
		return newsInfo;
	}

	public void setNewsInfo(String newsInfo) {
		this.newsInfo = newsInfo;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSources() {
		return sources;
	}

	public void setSources(String sources) {
		this.sources = sources;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}

	public boolean isPhotoNews() {
		return isPhotoNews;
	}

	public void setPhotoNews(boolean isPhotoNews) {
		this.isPhotoNews = isPhotoNews;
	}

	public boolean isTalk() {
		return isTalk;
	}

	public void setTalk(boolean isTalk) {
		this.isTalk = isTalk;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public boolean isLinkNews() {
		return isLinkNews;
	}

	public void setLinkNews(boolean isLinkNews) {
		this.isLinkNews = isLinkNews;
	}


}
