package com.jr.bank.dao.DAO;

public interface CardDAO {
	void changePw(String accounts,int pw);  	//������
	void savaMoney(double money, String accounts, int password); //��Ǯ
	void getMoney(double money,String accounts,int password);	//ȡǮ
	void transfer(String myaccounts,double money,int password,String heaccounts);//ת��
	void miss(String ID,int password);	//��ʧ
	void exit();//�˳�ϵͳ
}
