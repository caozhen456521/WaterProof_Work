package com.qingzu.entity;

import java.io.Serializable;
/**
 * 用户基本信息
 * @author Administrator
 *
 */
public class Member implements Serializable {
	private int   id;//会员编号
	private int companyId;//所属公司--备用
	private int memberTypeId;//是否在找活列表展示(0展示，1不展示)
	private String userName;//用户名
	private String userPwd;//密码
	private String memberPhoto;//会员头像
	private boolean isOpen;//是否开通
	private int loginCount;//登录次数
	private String lastLoginIp;//最后登录IP
	private String lastLoginTime;//最后登录时间
	private String issueTime;//创建时间
	private String roleList;//角色集逗号分开
	private int raffleNumber;//抽奖次数--备用
	private int dealNumber;//成交次数
	private int cancelNumber;//取消次数
	private int regionId;//所属城市Id
	private String nickName;//会员昵称
	private String recommendCode;//会员邀请码
	private int pushCount;//发布招工信息量
	private int recommendCount;//推荐会员量
	private int memberSex;//会员性别（0是男，1是女，2是保密）
	private String contactAddress;//联系地址
	private int isCheck;//是否待审（0没有申请实名认证,1实名认证待审）
	private String levelName;//会员等级（普通、vip）--备用
	private double levelNumber;//等级星数--备用
	private int  integralNumber;//积分值
	private double walletMoney;//钱包资金--备用
	private String updateTime;//更新时间
	private double workerLevel;//工人信誉星数
	private double bossLevel;//老板信誉星数
	private String birthday;//会员生日
	private String regionPath;//城市路径
	private String pushGeTuiId;//个推客户Id--备用
	private String pushJiGuangId;//极光客户Id--备用
	private String realName;//真实姓名
	private String contactName;//联系人
	private String contactTel;//联系电话
	private boolean isRealName;//是否实名认证
	private int isFreeze;//冻结状态(0是正常，1帐号冻结)
	private int defaultRoleId;//默认角色Id(最后登录选择的角色)
	private int isPayingMembers;//是否付费会员(0不是，1是付10000元/年付费会员)




	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getMemberTypeId() {
		return memberTypeId;
	}

	public void setMemberTypeId(int memberTypeId) {
		this.memberTypeId = memberTypeId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getMemberPhoto() {
		return memberPhoto;
	}

	public void setMemberPhoto(String memberPhoto) {
		this.memberPhoto = memberPhoto;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public String getRoleList() {
		return roleList;
	}

	public void setRoleList(String roleList) {
		this.roleList = roleList;
	}

	public int getRaffleNumber() {
		return raffleNumber;
	}

	public void setRaffleNumber(int raffleNumber) {
		this.raffleNumber = raffleNumber;
	}

	public int getDealNumber() {
		return dealNumber;
	}

	public void setDealNumber(int dealNumber) {
		this.dealNumber = dealNumber;
	}

	public int getCancelNumber() {
		return cancelNumber;
	}

	public void setCancelNumber(int cancelNumber) {
		this.cancelNumber = cancelNumber;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRecommendCode() {
		return recommendCode;
	}

	public void setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
	}

	public int getPushCount() {
		return pushCount;
	}

	public void setPushCount(int pushCount) {
		this.pushCount = pushCount;
	}

	public int getRecommendCount() {
		return recommendCount;
	}

	public void setRecommendCount(int recommendCount) {
		this.recommendCount = recommendCount;
	}

	public int getMemberSex() {
		return memberSex;
	}

	public void setMemberSex(int memberSex) {
		this.memberSex = memberSex;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public int getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public double getLevelNumber() {
		return levelNumber;
	}

	public void setLevelNumber(double levelNumber) {
		this.levelNumber = levelNumber;
	}

	

	

	public int getIntegralNumber() {
		return integralNumber;
	}

	public void setIntegralNumber(int integralNumber) {
		this.integralNumber = integralNumber;
	}

	public double getWalletMoney() {
		return walletMoney;
	}

	public void setWalletMoney(double walletMoney) {
		this.walletMoney = walletMoney;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public double getWorkerLevel() {
		return workerLevel;
	}

	public void setWorkerLevel(double workerLevel) {
		this.workerLevel = workerLevel;
	}

	public double getBossLevel() {
		return bossLevel;
	}

	public void setBossLevel(double bossLevel) {
		this.bossLevel = bossLevel;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getRegionPath() {
		return regionPath;
	}

	public void setRegionPath(String regionPath) {
		this.regionPath = regionPath;
	}

	public String getPushGeTuiId() {
		return pushGeTuiId;
	}

	public void setPushGeTuiId(String pushGeTuiId) {
		this.pushGeTuiId = pushGeTuiId;
	}

	public String getPushJiGuangId() {
		return pushJiGuangId;
	}

	public void setPushJiGuangId(String pushJiGuangId) {
		this.pushJiGuangId = pushJiGuangId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public boolean isRealName() {
		return isRealName;
	}

	public void setRealName(boolean isRealName) {
		this.isRealName = isRealName;
	}

	public int getIsFreeze() {
		return isFreeze;
	}

	public void setIsFreeze(int isFreeze) {
		this.isFreeze = isFreeze;
	}

	public int getDefaultRoleId() {
		return defaultRoleId;
	}

	public void setDefaultRoleId(int defaultRoleId) {
		this.defaultRoleId = defaultRoleId;
	}

	public int getIsPayingMembers() {
		return isPayingMembers;
	}

	public void setIsPayingMembers(int isPayingMembers) {
		this.isPayingMembers = isPayingMembers;
	}

}
