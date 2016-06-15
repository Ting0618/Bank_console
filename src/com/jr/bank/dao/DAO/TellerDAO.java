package com.jr.bank.dao.DAO;

public interface TellerDAO{
	void openCard(String id,String name,double money);  //开卡
	void delete(String ID,int password);		//销户
	void display(String id);		//查看用户的所有信息
	void rejister(String id,String pw);		//登录管理系统
	void openInBank(String accounts,int password);	//开通网银
	void exit();//退出系统
}