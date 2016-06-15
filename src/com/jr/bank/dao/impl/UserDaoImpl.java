package com.jr.bank.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.jr.Link.Link;
import com.jr.bank.dao.DAO.UserDAO;

public class UserDaoImpl implements UserDAO {
	Scanner scan = new Scanner(System.in);

	// --------查询余额
	public void display(String username,int password) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Link.getInstance().getCon();// 创建连接
			String str = "select card.uid,yesno from card,user where password=? and ID=? or uname=? or accounts=?";
			pst = con.prepareStatement(str); // 从数据库中找出uid和yesno
			pst.setInt(1, password);
			pst.setString(2, username);
			pst.setString(3, username);
			pst.setString(4, username);
			//pst.setString(1, accounts); // 给第1，2个问号设置具体的值
			rs = pst.executeQuery();
			if (rs.next()) { // 如果uid和yesno存在的话
				if (rs.getString("yesno").equals("yes")) { // 如果yesno为yes则卡可以用
					int ss = rs.getInt("uid"); // 将uid取出来用于查找的条件
					String sql = "select name,money,accounts from user,card where user.uid = ? and card.uid =?";
					pst = con.prepareStatement(sql);
					pst.setInt(1, ss);
					pst.setInt(2, ss);
					rs = pst.executeQuery();
					while (rs.next()) { // 查找到后将对应的值无出来
						String n = rs.getString("name");
						String a = rs.getString("accounts");
						double m = rs.getDouble("money");
						System.out.println("姓名: " + n + "卡号:" + a + "\t"
								+ "余额: " + m);
					}
				} else { // 如果yesno为no的话则卡已挂失
					System.out.println("此卡已挂失");
				}
			} else { // 找不到uid和yesno说明帐号与密码不匹配
				System.out.println("密码错误！");
				System.out.println("请重新输入密码");
				password = scan.nextInt();
				display(username, password);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}
	
	//转账
	public void transfer(String username, double money, int password, String heac) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Link.getInstance().getCon(); // 创建连接
			con.setAutoCommit(false); // 取消自动提交
			String sql = "select money,yesno,user.uid from user,card where password=? and user.uid=card.uid and ID=? or uname=? or accounts=?";// 根据密码和ID找到money和yesno
			pst = con.prepareStatement(sql);
			pst.setInt(1, password);
			pst.setString(2, username);
			pst.setString(3, username);
			pst.setString(4, username);
			rs = pst.executeQuery();
			if (rs.next()) { // 如果找到找到money和yesno
				if (rs.getString("yesno").equals("yes")) { // 如果此卡能用
					double m = rs.getDouble("money"); // 判断金额是否足够转出
					if (m < money) {
						System.out.println("余额不足,转账失败");
					} else { // 如果够转出的话
						int i = rs.getInt("uid");
						double money1 = money * 5 / 2000;
						double money2 = money + money1;
						String sq = "update card set money = money-? where uid=?"; // 从自己的卡上转出
						pst = con.prepareStatement(sq);
						pst.setDouble(1, money2);
						pst.setInt(2, i);
						pst.executeUpdate();
						String str = "select uid from card where accounts=?"; // 找出对方卡号
						pst = con.prepareStatement(str);
						pst.setString(1, heac);
						rs = pst.executeQuery();
						if (rs.next()) { // 如果对方卡号存在的话
							int u = rs.getInt("uid");
							String s = "update card set money = money+" + money
									+ " where uid=?"; // 对方卡上金额加对应的money
							pst = con.prepareStatement(s);
							pst.setInt(1, u);
							pst.executeUpdate();
							System.out.println("转账成功!手续费：" + money1);
							con.commit();
						} else { // 如果对方卡号不存在的话，重新输入
							System.out.println("您要转入的账户不存在，请重新输入！");
							String accounts = scan.next();
							transfer(username, money, password, accounts);
						}
					}
				}

				else {
					System.out.println("此卡也挂失，暂不支持转账");
				}
			} else { // 如果没找到money和yesno说明输入错误，用递归的方法重新在输一次
				System.out.println("您的密码输入错误！");
				System.out.println("请重新输入密码");
				password = scan.nextInt();
				transfer(username, money, password, heac);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//登录
	public void rejister(String username,String upassword){
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = (Connection) Link.getInstance().getCon();
			String sql = "select accounts,ID from card,user where upassword=? and accounts=? or uname=? or ID=?";
			pst = con.prepareStatement(sql);
			pst.setString(1, upassword);
			pst.setString(2, username);
			pst.setString(3, username);
			pst.setString(4, username);
			rs = pst.executeQuery();
			if(rs.next()){
				System.out.println("登录成功！欢迎登录网银系统");
			}
			else{
				System.out.println("用户名或密码错误！");
				System.out.println("请重新输入用户名");
				username = scan.next();
				System.out.println("请重新输入网银密码");
				upassword = scan.next();
				rejister(username,upassword);
			}
		}
		catch (SQLException e) {
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