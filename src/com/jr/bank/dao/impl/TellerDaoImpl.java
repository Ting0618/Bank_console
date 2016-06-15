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

	public void openCard(String id, String name, double money) { // ����
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = (Connection) Link.getInstance().getCon();
			String sql = "insert into user(ID,name) values(?,?)"; // ��User���в������֤�ź�����
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
			
			String sq2 = "insert into card(money) values(?) where uid =? "; // ��Card���в����ʼ���
			pst = con.prepareStatement(sq2);
			pst.setDouble(1, money);
			pst.setInt(2, uid);
			pst.executeUpdate();
			String sq = "select accounts from card,user where card.uid=? and user.uid=card.uid";// ��Card�����ҳ�����Ų���ӡ����
			pst = con.prepareStatement(sq);
			pst.setInt(1, uid);
			rs = pst.executeQuery();
			if (rs.next()) {
				String a = rs.getString("accounts");
				System.out.println("�����ɹ����ʺ�Ϊ" + a);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}

	// ��ͨ����
	public void openInBank(String accounts, int password) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = (Connection) Link.getInstance().getCon();
			String sql = "select yesno,interbank,card.uid from card,user where accounts=? and password =? and user.uid=card.uid";
			pst = con.prepareStatement(sql); // �ҳ� yesno,interbank �� card.uid
			pst.setString(1, accounts);
			pst.setInt(2, password);
			rs = pst.executeQuery();
			if (rs.next()) {
				String s = rs.getString("yesno");
				String inter = rs.getString("interbank");
				int uid = rs.getInt("card.uid");
				if (s.equals("yes") == true) { // yesno�������yes�Ļ���ʾ����
					if (inter.equals("no") == true) { // interbank�������no�Ļ���ʾδ��ͨ����
						String sq = "update card set interbank ='yes' where uid =?"; // ��noת����yes����ʾ��ͨ������
						pst = con.prepareStatement(sq);
						pst.setInt(1, uid);
						pst.executeUpdate();
						System.out.println("�����������ǳ�");
						String uname = scan.next();
						System.out.println("�������������루������������ģ����֣���ĸ��");
						String upassword = scan.next();
						String sl = "update user set uname =?,upassword=? where uid=?"; // �����ǳƺ���������
						pst = con.prepareStatement(sl);
						pst.setString(1, uname);
						pst.setString(2, upassword);
						pst.setInt(3, uid);
						pst.executeUpdate();
						System.out.println("��ͨ�ɹ���");
					} else {
						System.out.println("�˿��ѿ�ͨ����");
					}
				} else { // yesno���������yes�Ļ���ʾ������
					System.out.println("�˿��ѹ�ʧ�����ɿ�ͨ����");
				}
			} else {
				System.out.println("������ʺ��������");
				System.out.println("�����������ʺ�");
				accounts = scan.next();
				System.out.println("��������������");
				password = scan.nextInt();
				openInBank(accounts, password);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void delete(String accounts, int password) { // ����
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = (Connection) Link.getInstance().getCon();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("��ȷ����������");
		int pw2 = scan.nextInt();
		if (password == pw2) { 
			String sql1 = "select user.uid from user,card where accounts=? and password =? and user.uid=card.uid";
			try {
				pst = con.prepareStatement(sql1);
				pst.setString(1, accounts);
				pst.setInt(2, password);
				rs = pst.executeQuery();
				if (rs.next()) { // �����ҵ���uidɾ��
					int uid = rs.getInt("user.uid");
					String sql = "update card set uid=1 where uid = ?";
					pst = con.prepareStatement(sql);
					pst.setInt(1, uid);
					pst.executeUpdate();
					String sql2 = "delete from user where uid ='" + uid + "'";
					pst = con.prepareStatement(sql2);
					pst.executeUpdate();
					System.out.println("�ʺ�������");
				} else {
					System.out.println("�ʺŻ������������\n�����������ʺ�");
					accounts = scan.next();
					System.out.println("��������������");
					password = scan.nextInt();
					delete(accounts, password);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else{
			System.out.println("�������벻һ����\n�����������ʺ�");
			accounts = scan.next();
			System.out.println("��������������");
			password = scan.nextInt();
			delete(accounts, password);
		}
		Link.getInstance().closeAll(con, pst, rs);
	}

	// ��¼����Աϵͳ
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
				System.out.println("��ӭ�������Աϵͳ�� " + rs.getString("name"));
			} else {
				System.out.println("������ʺŴ���\n�����������ʺ�");
				id = scan.next();
				System.out.println("��������������");
				pw = scan.next();
				rejister(id, pw);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}

	// ��ѯ�û��ʺŵ�������Ϣ
	public void display(String id) { // �������֤�Ž��в�ѯ
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = (Connection) Link.getInstance().getCon();
			String sql = "select uid from user where ID = ?";
			pst = con.prepareStatement(sql);// �ҳ�uid
			pst.setString(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				int ss = rs.getInt("uid");
				String s = "select * from user,card where card.uid =? and user.uid=?";
				pst = con.prepareStatement(s);// ����uid��ѯ�û���������Ϣ
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
					System.out.println("���֤: " + i + "\t" + "����: " + n + "����:"
							+ a + "\t" + "���: " + m + "\t�Ƿ�ͨ����" + in
							+ "\t�����ǳ�" + u);
				}
			} else {
				System.out.println("���֤������������������룡");
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
		System.out.println("�˳��ɹ�");
		System.exit(0);
	}
}
