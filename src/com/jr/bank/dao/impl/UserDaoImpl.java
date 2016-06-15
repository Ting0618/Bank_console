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

	// --------��ѯ���
	public void display(String username,int password) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Link.getInstance().getCon();// ��������
			String str = "select card.uid,yesno from card,user where password=? and ID=? or uname=? or accounts=?";
			pst = con.prepareStatement(str); // �����ݿ����ҳ�uid��yesno
			pst.setInt(1, password);
			pst.setString(2, username);
			pst.setString(3, username);
			pst.setString(4, username);
			//pst.setString(1, accounts); // ����1��2���ʺ����þ����ֵ
			rs = pst.executeQuery();
			if (rs.next()) { // ���uid��yesno���ڵĻ�
				if (rs.getString("yesno").equals("yes")) { // ���yesnoΪyes�򿨿�����
					int ss = rs.getInt("uid"); // ��uidȡ�������ڲ��ҵ�����
					String sql = "select name,money,accounts from user,card where user.uid = ? and card.uid =?";
					pst = con.prepareStatement(sql);
					pst.setInt(1, ss);
					pst.setInt(2, ss);
					rs = pst.executeQuery();
					while (rs.next()) { // ���ҵ��󽫶�Ӧ��ֵ�޳���
						String n = rs.getString("name");
						String a = rs.getString("accounts");
						double m = rs.getDouble("money");
						System.out.println("����: " + n + "����:" + a + "\t"
								+ "���: " + m);
					}
				} else { // ���yesnoΪno�Ļ����ѹ�ʧ
					System.out.println("�˿��ѹ�ʧ");
				}
			} else { // �Ҳ���uid��yesno˵���ʺ������벻ƥ��
				System.out.println("�������");
				System.out.println("��������������");
				password = scan.nextInt();
				display(username, password);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}
	
	//ת��
	public void transfer(String username, double money, int password, String heac) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Link.getInstance().getCon(); // ��������
			con.setAutoCommit(false); // ȡ���Զ��ύ
			String sql = "select money,yesno,user.uid from user,card where password=? and user.uid=card.uid and ID=? or uname=? or accounts=?";// ���������ID�ҵ�money��yesno
			pst = con.prepareStatement(sql);
			pst.setInt(1, password);
			pst.setString(2, username);
			pst.setString(3, username);
			pst.setString(4, username);
			rs = pst.executeQuery();
			if (rs.next()) { // ����ҵ��ҵ�money��yesno
				if (rs.getString("yesno").equals("yes")) { // ����˿�����
					double m = rs.getDouble("money"); // �жϽ���Ƿ��㹻ת��
					if (m < money) {
						System.out.println("����,ת��ʧ��");
					} else { // �����ת���Ļ�
						int i = rs.getInt("uid");
						double money1 = money * 5 / 2000;
						double money2 = money + money1;
						String sq = "update card set money = money-? where uid=?"; // ���Լ��Ŀ���ת��
						pst = con.prepareStatement(sq);
						pst.setDouble(1, money2);
						pst.setInt(2, i);
						pst.executeUpdate();
						String str = "select uid from card where accounts=?"; // �ҳ��Է�����
						pst = con.prepareStatement(str);
						pst.setString(1, heac);
						rs = pst.executeQuery();
						if (rs.next()) { // ����Է����Ŵ��ڵĻ�
							int u = rs.getInt("uid");
							String s = "update card set money = money+" + money
									+ " where uid=?"; // �Է����Ͻ��Ӷ�Ӧ��money
							pst = con.prepareStatement(s);
							pst.setInt(1, u);
							pst.executeUpdate();
							System.out.println("ת�˳ɹ�!�����ѣ�" + money1);
							con.commit();
						} else { // ����Է����Ų����ڵĻ�����������
							System.out.println("��Ҫת����˻������ڣ����������룡");
							String accounts = scan.next();
							transfer(username, money, password, accounts);
						}
					}
				}

				else {
					System.out.println("�˿�Ҳ��ʧ���ݲ�֧��ת��");
				}
			} else { // ���û�ҵ�money��yesno˵����������õݹ�ķ�����������һ��
				System.out.println("���������������");
				System.out.println("��������������");
				password = scan.nextInt();
				transfer(username, money, password, heac);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//��¼
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
				System.out.println("��¼�ɹ�����ӭ��¼����ϵͳ");
			}
			else{
				System.out.println("�û������������");
				System.out.println("�����������û���");
				username = scan.next();
				System.out.println("������������������");
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
		System.out.println("�˳��ɹ�");
		System.exit(0);
	}
}