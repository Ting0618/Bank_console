package com.jr.bank.dao.DAO;

public interface TellerDAO{
	void openCard(String id,String name,double money);  //����
	void delete(String ID,int password);		//����
	void display(String id);		//�鿴�û���������Ϣ
	void rejister(String id,String pw);		//��¼����ϵͳ
	void openInBank(String accounts,int password);	//��ͨ����
	void exit();//�˳�ϵͳ
}