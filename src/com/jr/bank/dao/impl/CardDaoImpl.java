package com.jr.bank.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.jr.Link.Link;
import com.jr.bank.dao.DAO.CardDAO;

public class CardDaoImpl implements CardDAO {
	Scanner scan = new Scanner(System.in);

	public void changePw(String accounts, int password) {		//修改密码
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Link.getInstance().getCon();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("请设置新密码");
		String pw1 = scan.next();
		System.out.println("请再输入一次");
		String pw2 = scan.next();
		if (pw1.equals(pw2)) {
			System.out.println("请再输入一次");
			String pw3 = scan.next();
			if (pw1.equals(pw3) && pw2.equals(pw3)) {

				String sql = "update card set password ='" + pw3
						+ "' where accounts = ? and password =?";
				try {
					pst = con.prepareStatement(sql);
					pst.setString(1, accounts);
					pst.setInt(2, password);
					pst.executeUpdate();
					System.out.println("----------修改成功----------");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Link.getInstance().closeAll(con, pst, rs);
			} else {
				System.out.println("第三次密码和前两次不一样");
			}
		} else {
			System.out.println("两次密码不一致");
		}
	}

	// -----------存钱
	public void savaMoney(double money, String accounts, int password) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Link.getInstance().getCon();// 建立连接
			String sql = "select yesno,user.uid from user,card where accounts=? and password=? and user.uid=card.uid";// 找出yesno
			pst = con.prepareStatement(sql);
			pst.setString(1, accounts);
			pst.setInt(2, password); // 给1，2两个问号具体 的值
			rs = pst.executeQuery();
			if (rs.next()) {
				if (rs.getString("yesno").equals("yes")) { // 如果yesno为yes则卡可以用
					int u = rs.getInt("uid");
					String sq = "update card set money = money+" + money
							+ " where uid=" + u + ""; // 刷新
					pst = con.prepareStatement(sq);
					pst.executeUpdate();
					System.out.println("存款成功");
				} else { // 如果yesno为no则卡不可以用
					System.out.println("此卡已挂失，暂不支持存款");
				}
			} else { // 没找出yesno时
				System.out.println("帐号或密码错误！请重新输入");
				System.out.println("请重新输入卡号");
				String ID = scan.next();
				System.out.println("请重新输入密码");
				password = scan.nextInt();
				savaMoney(money, ID, password);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}

	// -------------取钱
	public void getMoney(double money, String accounts, int password) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Link.getInstance().getCon();// 建立连接
			String sql = "select money,yesno,user.uid from user,card where accounts=? and password=? and user.uid=card.uid";// 找出money和yesno
			pst = con.prepareStatement(sql);
			pst.setString(1, accounts);
			pst.setInt(2, password); // 给1，2两个问号具体的值
			rs = pst.executeQuery();
			if (rs.next()) {
				if (rs.getString("yesno").equals("yes")) { // 如果yesno不为no则卡可以用
					double m = rs.getDouble("money");
					int a = rs.getInt("uid");
					if (m < money) { // 余额是否够取出
						System.out.println("余额不足");
					} else { // 余额足够时进行
						String sq = "update card set money = money-" + money
								+ " where uid='" + a + "'"; // 刷新
						pst = con.prepareStatement(sq);
						pst.executeUpdate();
						System.out.println("取款成功");
					}
				} else { // 如果yesno为no则卡不可以用
					System.out.println("此卡已挂失，暂不支持取款");
				}
			} else { // 没找出money和yesno时
				System.out.println("帐号或密码错误！");
				System.out.println("请重新输入金额");
				money = scan.nextDouble();
				System.out.println("请重新输入卡号");
				accounts = scan.next();
				System.out.println("请重新输入密码");
				password = scan.nextInt();
				savaMoney(money, accounts, password);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}

	// -------------------------------转账

	public void transfer(String myac, double money, int password, String heac) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Link.getInstance().getCon(); // 创建连接
			con.setAutoCommit(false); // 取消自动提交
			String sql = "select money,yesno,user.uid from user,card where accounts=? and password=? and user.uid=card.uid";// 根据密码和ID找到money和yesno
			pst = con.prepareStatement(sql);
			pst.setString(1, myac);
			pst.setInt(2, password);
			rs = pst.executeQuery();
			if (rs.next()) { // 如果找到找到money和yesno
				if (rs.getString("yesno").equals("yes")) { // 如果此卡能用
					double m = rs.getDouble("money"); // 判断金额是否足够转出
					if (m < money) {
						System.out.println("余额不足,转账失败");
					} else { // 如果够转出的话
						int i = rs.getInt("uid");
						double money1 = money * 5 / 1000;
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
							System.out.println("转账成功!手续费：" + money1+" 元");
							con.commit();
						} else { // 如果对方卡号不存在的话，重新输入
							System.out.println("您要转入的账户不存在，请重新输入！");
							String accounts = scan.next();
							transfer(myac, money, password, accounts);
						}
					}
				}

				else {
					System.out.println("此卡也挂失，暂不支持转账");
				}
			} else { // 如果没找到money和yesno说明输入错误，用递归的方法重新在输一次
				System.out.println("您的帐号或密码输入错误！");
				System.out.println("请重新输入帐号");
				myac = scan.next();
				System.out.println("请重新输入密码");
				password = scan.nextInt();
				transfer(myac, money, password, heac);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}
	
	// ----------------------------挂失
		public void miss(String ID, int password) {
			Connection con = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
			try {
				con = (Connection) Link.getInstance().getCon(); // 创建连接
				String sql = "select user.uid ,yesno from card,user where ID=? and password =? ";// 找到uid和yesno
				pst = con.prepareStatement(sql);// 执行sql语句
				pst.setString(1, ID);
				pst.setInt(2, password); // 设置两个问号中的内容
				rs = pst.executeQuery();
				if (rs.next()) { // 如果找到uid了
					int i = rs.getInt("uid"); // 取出uid，用做修改Card表的条件
					String s = "update card set yesno = 'no' where uid =?";
					pst = con.prepareStatement(s);
					pst.setInt(1, i);
					pst.executeUpdate();
					System.out.println("挂失成功");
				} else { // 如果没找到uid说明身份证号和密码不匹配
					System.out.println("密码或帐号错误！\n请重新输入身份证号");
					ID = scan.next();
					System.out.println("请重新输入密码");
					password = scan.nextInt();
					miss(ID, password); // 重新设置身份证号和密码后在进行操作
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
		
		/**
		 * 
		 * 判断密码与帐号是否匹配，帐号是否挂失
		 */
		public boolean match(String accounts,int password){
			Connection con = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
			boolean b = false;
			try {
				con = (Connection) Link.getInstance().getCon();
				String sql = "select accounts,password,yesno from card where accounts=? and password=?";
				pst = con.prepareStatement(sql);
				pst.setString(1, accounts);
				pst.setInt(2, password);
				rs = pst.executeQuery();
				if(rs.next()){
					String y = rs.getString("yesno");
					if(y.equals("yes")){
						b = true;
					}
					else{
						System.out.println("该卡已挂失！");
						b = false;
					}
				}
				else{
					System.out.println("帐号或密码输入错误！请认真核对！");
					b = false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			Link.getInstance().closeAll(con, pst, rs);
			return b;
		}
}