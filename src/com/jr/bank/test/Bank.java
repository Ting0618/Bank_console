package com.jr.bank.test;

import java.util.Scanner;

import com.jr.bank.dao.impl.CardDaoImpl;
import com.jr.bank.dao.impl.TellerDaoImpl;
import com.jr.bank.dao.impl.UserDaoImpl;
import com.jr.bank.menu.Menu;

public class Bank {
	public static void main(String[] args) {
		String ID;
		int password;
		String accounts;
		double money;
		TellerDaoImpl teller = new TellerDaoImpl();
		UserDaoImpl user = new UserDaoImpl();
		CardDaoImpl card = new CardDaoImpl();
		Menu menu = new Menu();
		Scanner scan = new Scanner(System.in);

		System.out.println("��ѡ����Ҫ�����ϵͳ");
		System.out.println("1  �������Աϵͳ");
		System.out.println("2  ��������ϵͳ");
		int n = scan.nextInt();
		switch (n) {
		case 1:
			System.out.println("�����������ʺ�");
			String id = scan.next();
			System.out.println("�����������ʺ�����");
			String tpw = scan.next();
			teller.rejister(id, tpw);
			while (true) {
				menu.tellerMenu();
				n = scan.nextInt();
				switch (n) {
				case 1:
					System.out.println("�����뻧�����֤��");
					String uid = scan.next();
					System.out.println("����������");
					String name = scan.next();
					System.out.println("�������ʼ��");
					money = scan.nextDouble();
					teller.openCard(uid, name, money);
					break;
				case 2:
					System.out.println("�������ʺ�");
					accounts = scan.next();
					System.out.println("������ԭʼ����");
					password = scan.nextInt();
					card.changePw(accounts, password);
					break;
				case 3:
					System.out.println("������������Ľ��");
					money = scan.nextDouble();
					System.out.println("������������Ŀ���");
					ID = scan.next();
					System.out.println("��������������");
					password = scan.nextInt();
					card.savaMoney(money, ID, password);
					break;
				case 4:
					System.out.println("������ȡ����");
					money = scan.nextDouble();
					System.out.println("����������");
					int password2 = scan.nextInt();
					System.out.println("������ȡ���");
					accounts = scan.next();
					card.getMoney(money,accounts, password2);
					break;
				case 5:
					System.out.println("�����������ʺ�");
					accounts = scan.next();
					System.out.println("���������룺");
					password = scan.nextInt();
					user.display(accounts, password);
					break;
				case 6:
					System.out.println("������Ҫ���ٵ��ʺ�");
					accounts = scan.next();
					System.out.println("������Ҫ�����ʺ�����");
					password = scan.nextInt();
					teller.delete(accounts, password);
					break;
				case 7:
					System.out.println("��������Ҫ��ѯ�û������֤");
					ID = scan.next();
					teller.display(ID);
					break;
				case 8:
					System.out.println("���������Ŀ���");
					accounts = scan.next();
					System.out.println("����������ת�����");
					money = scan.nextDouble();
					System.out.println("��������������");
					password = scan.nextInt();
					System.out.println("������ת�뷽�Ŀ���");
					String accounts2 = scan.next();
					card.transfer(accounts, money, password, accounts2);
					break;
				case 9:
					System.out.println("�������ʧ�����֤�ţ�");
					ID = scan.next();
					System.out.println("�������ʧ�������룺");
					password = scan.nextInt();
					card.miss(ID, password);
					break;
				case 10:
					System.out.println("������Ҫ��ͨ���ʺţ�");
					accounts = scan.next();
					System.out.println("�������ʺ�����");
					password = scan.nextInt();
					teller.openInBank(accounts, password);
					break;
				case 11:
					teller.exit();
					break;
				default:
					System.out.println("�������111");
				}
			}
			//break;
		case 2:
			System.out.println("�����������û���(�û������������֤�ţ����ţ������ǳ�)");
			String username = scan.next();
			System.out.println("������������������");
			String upassword = scan.next();
			user.rejister(username, upassword);
			while (true) {
				menu.userMenu();
				n = scan.nextInt();
				switch (n) {
				case 1:
					System.out.println("�������������룺");
					password = scan.nextInt();
					user.display(username, password);
					break;
				case 2:
					System.out.println("����������ת�����");
					money = scan.nextDouble();
					System.out.println("�����������ʺ�����");
					password = scan.nextInt();
					System.out.println("������ת�뷽�Ŀ���");
					String accounts2 = scan.next();
					user.transfer(username, money, password, accounts2);
					break;
				case 3:
					user.exit();
					break;
					default:
				}
			}
		default:
		}
	}
}
