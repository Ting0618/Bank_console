package com.jr.bank.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.jr.Link.Link;
import com.jr.bank.dao.DAO.TellerDAO;
import com.mysql.jdbc.Connection;

public class TellerDaoImpl implements TellerDAO {
	Scanner scan = new Scanner(System.in);

	public void openCard(String id, String name, double money) { // 开卡
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = (Connection) Link.getInstance().getCon();
			String sql = "insert into user(ID,name) values(?,?)"; // 在User表中插入身份证号和姓名
			pst = con.prepareStatement(sql);
			pst.setString(1, id);
			pst.setString(2, name);
			pst.executeUpdate();
			System.out.println("jjjjjj");
			String sql3 = "select uid from user where ID=?";
			pst = con.prepareStatement(sql3);
			pst.setString(1, id);
			rs = pst.executeQuery();
			int uid = 0;
			while(rs.next()){
				uid = rs.getInt("uid");
			}
			String sql4 = "select uid from card where uid=?";
			pst = con.prepareStatement(sql4);
			pst.setInt(1, 1);
			rs = pst.executeQuery();
			while(rs.next()){
				String sql5 = "update card set uid = ?";
				pst = con.prepareStatement(sql5);
				pst.setInt(1, uid);
				pst.executeUpdate();
				System.out.println("flash okokok");
				break;
			}
			
			String sq2 = "insert into card(money) values(?) where uid =? "; // 在Card表中插入初始金额
			pst = con.prepareStatement(sq2);
			pst.setDouble(1, money);
			pst.setInt(2, uid);
			pst.executeUpdate();
			String sq = "select accounts from card,user where card.uid=? and user.uid=card.uid";// 在Card表中找出对杨卡号并打印出来
			pst = con.prepareStatement(sq);
			pst.setInt(1, uid);
			rs = pst.executeQuery();
			if (rs.next()) {
				String a = rs.getString("accounts");
				System.out.println("开卡成功，帐号为" + a);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}

	// 开通网银
	public void openInBank(String accounts, int password) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = (Connection) Link.getInstance().getCon();
			String sql = "select yesno,interbank,card.uid from card,user where accounts=? and password =? and user.uid=card.uid";
			pst = con.prepareStatement(sql); // 找出 yesno,interbank 和 card.uid
			pst.setString(1, accounts);
			pst.setInt(2, password);
			rs = pst.executeQuery();
			if (rs.next()) {
				String s = rs.getString("yesno");
				String inter = rs.getString("interbank");
				int uid = rs.getInt("card.uid");
				if (s.equals("yes") == true) { // yesno中如果是yes的话表示能用
					if (inter.equals("no") == true) { // interbank中如果是no的话表示未开通网银
						String sq = "update card set interbank ='yes' where uid =?"; // 将no转化成yes，表示开通了网银
						pst = con.prepareStatement(sq);
						pst.setInt(1, uid);
						pst.executeUpdate();
						System.out.println("请设置网银昵称");
						String uname = scan.next();
						System.out.println("请设置网银密码（密码可以是中文，数字，字母）");
						String upassword = scan.next();
						String sl = "update user set uname =?,upassword=? where uid=?"; // 插入昵称和网银密码
						pst = con.prepareStatement(sl);
						pst.setString(1, uname);
						pst.setString(2, upassword);
						pst.setInt(3, uid);
						pst.executeUpdate();
						System.out.println("开通成功！");
					} else {
						System.out.println("此卡已开通网银");
					}
				} else { // yesno中如果不是yes的话表示不能用
					System.out.println("此卡已挂失，不可开通网银");
				}
			} else {
				System.out.println("密码或帐号输入错误");
				System.out.println("请重新输入帐号");
				accounts = scan.next();
				System.out.println("请重新输入密码");
				password = scan.nextInt();
				openInBank(accounts, password);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void delete(String accounts, int password) { // 销户
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = (Connection) Link.getInstance().getCon();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("请确认输入密码");
		int pw2 = scan.nextInt();
		if (password == pw2) { 
			String sql1 = "select user.uid from user,card where accounts=? and password =? and user.uid=card.uid";
			try {
				pst = con.prepareStatement(sql1);
				pst.setString(1, accounts);
				pst.setInt(2, password);
				rs = pst.executeQuery();
				if (rs.next()) { // 根据找到的uid删除
					int uid = rs.getInt("user.uid");
					String sql = "update card set uid=1 where uid = ?";
					pst = con.prepareStatement(sql);
					pst.setInt(1, uid);
					pst.executeUpdate();
					String sql2 = "delete from user where uid ='" + uid + "'";
					pst = con.prepareStatement(sql2);
					pst.executeUpdate();
					System.out.println("帐号已消除");
				} else {
					System.out.println("帐号或密码输入错误\n请重新输入帐号");
					accounts = scan.next();
					System.out.println("请重新输入密码");
					password = scan.nextInt();
					delete(accounts, password);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else{
			System.out.println("两次密码不一样！\n请重新输入帐号");
			accounts = scan.next();
			System.out.println("请重新输入密码");
			password = scan.nextInt();
			delete(accounts, password);
		}
		Link.getInstance().closeAll(con, pst, rs);
	}

	// 登录管理员系统
	public void rejister(String id, String pw) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = (Connection) Link.getInstance().getCon();
			String sql = "select name from teller where password=? and id=?";
			pst = con.prepareStatement(sql);
			pst.setString(1, pw);
			pst.setString(2, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				System.out.println("欢迎进入管理员系统， " + rs.getString("name"));
			} else {
				System.out.println("密码或帐号错误\n请重新输入帐号");
				id = scan.next();
				System.out.println("请重新输入密码");
				pw = scan.next();
				rejister(id, pw);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}

	// 查询用户帐号的所有信息
	public void display(String id) { // 输入身份证号进行查询
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = (Connection) Link.getInstance().getCon();
			String sql = "select uid from user where ID = ?";
			pst = con.prepareStatement(sql);// 找出uid
			pst.setString(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				int ss = rs.getInt("uid");
				String s = "select * from user,card where card.uid =? and user.uid=?";
				pst = con.prepareStatement(s);// 根据uid查询用户的所有信息
				pst.setInt(1, ss);
				pst.setInt(2, ss);
				rs = pst.executeQuery();
				while (rs.next()) {
					String i = rs.getString("ID");
					String n = rs.getString("name");
					String a = rs.getString("accounts");
					String m = rs.getString("money");
					String in = rs.getString("interbank");
					String u = rs.getString("uname");
					System.out.println("身份证: " + i + "\t" + "姓名: " + n + "卡号:"
							+ a + "\t" + "余额: " + m + "\t是否开通网银" + in
							+ "\t网银昵称" + u);
				}
			} else {
				System.out.println("身份证号输入错误，请重新输入！");
				Scanner scan = new Scanner(System.in);
				String i = scan.next();
				display(i);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}
	
	public void exit(){
		System.out.println("退出成功");
		System.exit(0);
	}
}
