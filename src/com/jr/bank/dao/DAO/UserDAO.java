package com.jr.bank.dao.DAO;

public interface UserDAO {
	void rejister(String uname,String upassword);	//��¼����
	void display(String accounts,int password);		//��ѯ���
	void transfer(String myaccounts,double money,int password,String heaccounts);//ת��
	void exit();//�˳�ϵͳ
}
