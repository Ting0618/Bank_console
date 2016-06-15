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

		System.out.println("请选择您要进入的系统");
		System.out.println("1  进入管理员系统");
		System.out.println("2  进入网银系统");
		int n = scan.nextInt();
		switch (n) {
		case 1:
			System.out.println("请输入您的帐号");
			String id = scan.next();
			System.out.println("请输入您的帐号密码");
			String tpw = scan.next();
			teller.rejister(id, tpw);
			while (true) {
				menu.tellerMenu();
				n = scan.nextInt();
				switch (n) {
				case 1:
					System.out.println("请输入户主身份证号");
					String uid = scan.next();
					System.out.println("请输入姓名");
					String name = scan.next();
					System.out.println("请输入初始余额：");
					money = scan.nextDouble();
					teller.openCard(uid, name, money);
					break;
				case 2:
					System.out.println("请输入帐号");
					accounts = scan.next();
					System.out.println("请输入原始密码");
					password = scan.nextInt();
					card.changePw(accounts, password);
					break;
				case 3:
					System.out.println("请输入您存入的金额");
					money = scan.nextDouble();
					System.out.println("请输入您存入的卡号");
					ID = scan.next();
					System.out.println("请输入您的密码");
					password = scan.nextInt();
					card.savaMoney(money, ID, password);
					break;
				case 4:
					System.out.println("请输入取款金额");
					money = scan.nextDouble();
					System.out.println("请输入密码");
					int password2 = scan.nextInt();
					System.out.println("请输入取款卡号");
					accounts = scan.next();
					card.getMoney(money,accounts, password2);
					break;
				case 5:
					System.out.println("请输入银行帐号");
					accounts = scan.next();
					System.out.println("请输入密码：");
					password = scan.nextInt();
					user.display(accounts, password);
					break;
				case 6:
					System.out.println("请输入要销毁的帐号");
					accounts = scan.next();
					System.out.println("请输入要销毁帐号密码");
					password = scan.nextInt();
					teller.delete(accounts, password);
					break;
				case 7:
					System.out.println("请输入您要查询用户的身份证");
					ID = scan.next();
					teller.display(ID);
					break;
				case 8:
					System.out.println("请输入您的卡号");
					accounts = scan.next();
					System.out.println("请输入您的转出金额");
					money = scan.nextDouble();
					System.out.println("请输入您的密码");
					password = scan.nextInt();
					System.out.println("请输入转入方的卡号");
					String accounts2 = scan.next();
					card.transfer(accounts, money, password, accounts2);
					break;
				case 9:
					System.out.println("请输入挂失的身份证号：");
					ID = scan.next();
					System.out.println("请输入挂失卡的密码：");
					password = scan.nextInt();
					card.miss(ID, password);
					break;
				case 10:
					System.out.println("请输入要开通的帐号：");
					accounts = scan.next();
					System.out.println("请输入帐号密码");
					password = scan.nextInt();
					teller.openInBank(accounts, password);
					break;
				case 11:
					teller.exit();
					break;
				default:
					System.out.println("输入错误！111");
				}
			}
			//break;
		case 2:
			System.out.println("请输入您的用户名(用户名可以是身份证号，卡号，网银昵称)");
			String username = scan.next();
			System.out.println("请输入您的网银密码");
			String upassword = scan.next();
			user.rejister(username, upassword);
			while (true) {
				menu.userMenu();
				n = scan.nextInt();
				switch (n) {
				case 1:
					System.out.println("请输入银行密码：");
					password = scan.nextInt();
					user.display(username, password);
					break;
				case 2:
					System.out.println("请输入您的转出金额");
					money = scan.nextDouble();
					System.out.println("请输入您的帐号密码");
					password = scan.nextInt();
					System.out.println("请输入转入方的卡号");
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
