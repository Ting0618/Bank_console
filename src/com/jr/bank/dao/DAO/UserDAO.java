package com.jr.bank.dao.DAO;

public interface UserDAO {
	void rejister(String uname,String upassword);	//登录网银
	void display(String accounts,int password);		//查询余额
	void transfer(String myaccounts,double money,int password,String heaccounts);//转账
	void exit();//退出系统
}
