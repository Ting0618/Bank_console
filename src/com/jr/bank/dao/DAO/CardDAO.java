package com.jr.bank.dao.DAO;

public interface CardDAO {
	void changePw(String accounts,int pw);  	//改密码
	void savaMoney(double money, String accounts, int password); //存钱
	void getMoney(double money,String accounts,int password);	//取钱
	void transfer(String myaccounts,double money,int password,String heaccounts);//转账
	void miss(String ID,int password);	//挂失
	void exit();//退出系统
}
