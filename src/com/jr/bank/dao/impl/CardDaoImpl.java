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

	public void changePw(String accounts, int password) {		//�޸�����
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Link.getInstance().getCon();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("������������");
		String pw1 = scan.next();
		System.out.println("��������һ��");
		String pw2 = scan.next();
		if (pw1.equals(pw2)) {
			System.out.println("��������һ��");
			String pw3 = scan.next();
			if (pw1.equals(pw3) && pw2.equals(pw3)) {

				String sql = "update card set password ='" + pw3
						+ "' where accounts = ? and password =?";
				try {
					pst = con.prepareStatement(sql);
					pst.setString(1, accounts);
					pst.setInt(2, password);
					pst.executeUpdate();
					System.out.println("----------�޸ĳɹ�----------");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Link.getInstance().closeAll(con, pst, rs);
			} else {
				System.out.println("�����������ǰ���β�һ��");
			}
		} else {
			System.out.println("�������벻һ��");
		}
	}

	// -----------��Ǯ
	public void savaMoney(double money, String accounts, int password) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Link.getInstance().getCon();// ��������
			String sql = "select yesno,user.uid from user,card where accounts=? and password=? and user.uid=card.uid";// �ҳ�yesno
			pst = con.prepareStatement(sql);
			pst.setString(1, accounts);
			pst.setInt(2, password); // ��1��2�����ʺž��� ��ֵ
			rs = pst.executeQuery();
			if (rs.next()) {
				if (rs.getString("yesno").equals("yes")) { // ���yesnoΪyes�򿨿�����
					int u = rs.getInt("uid");
					String sq = "update card set money = money+" + money
							+ " where uid=" + u + ""; // ˢ��
					pst = con.prepareStatement(sq);
					pst.executeUpdate();
					System.out.println("���ɹ�");
				} else { // ���yesnoΪno�򿨲�������
					System.out.println("�˿��ѹ�ʧ���ݲ�֧�ִ��");
				}
			} else { // û�ҳ�yesnoʱ
				System.out.println("�ʺŻ������������������");
				System.out.println("���������뿨��");
				String ID = scan.next();
				System.out.println("��������������");
				password = scan.nextInt();
				savaMoney(money, ID, password);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}

	// -------------ȡǮ
	public void getMoney(double money, String accounts, int password) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Link.getInstance().getCon();// ��������
			String sql = "select money,yesno,user.uid from user,card where accounts=? and password=? and user.uid=card.uid";// �ҳ�money��yesno
			pst = con.prepareStatement(sql);
			pst.setString(1, accounts);
			pst.setInt(2, password); // ��1��2�����ʺž����ֵ
			rs = pst.executeQuery();
			if (rs.next()) {
				if (rs.getString("yesno").equals("yes")) { // ���yesno��Ϊno�򿨿�����
					double m = rs.getDouble("money");
					int a = rs.getInt("uid");
					if (m < money) { // ����Ƿ�ȡ��
						System.out.println("����");
					} else { // ����㹻ʱ����
						String sq = "update card set money = money-" + money
								+ " where uid='" + a + "'"; // ˢ��
						pst = con.prepareStatement(sq);
						pst.executeUpdate();
						System.out.println("ȡ��ɹ�");
					}
				} else { // ���yesnoΪno�򿨲�������
					System.out.println("�˿��ѹ�ʧ���ݲ�֧��ȡ��");
				}
			} else { // û�ҳ�money��yesnoʱ
				System.out.println("�ʺŻ��������");
				System.out.println("������������");
				money = scan.nextDouble();
				System.out.println("���������뿨��");
				accounts = scan.next();
				System.out.println("��������������");
				password = scan.nextInt();
				savaMoney(money, accounts, password);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}

	// -------------------------------ת��

	public void transfer(String myac, double money, int password, String heac) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Link.getInstance().getCon(); // ��������
			con.setAutoCommit(false); // ȡ���Զ��ύ
			String sql = "select money,yesno,user.uid from user,card where accounts=? and password=? and user.uid=card.uid";// ���������ID�ҵ�money��yesno
			pst = con.prepareStatement(sql);
			pst.setString(1, myac);
			pst.setInt(2, password);
			rs = pst.executeQuery();
			if (rs.next()) { // ����ҵ��ҵ�money��yesno
				if (rs.getString("yesno").equals("yes")) { // ����˿�����
					double m = rs.getDouble("money"); // �жϽ���Ƿ��㹻ת��
					if (m < money) {
						System.out.println("����,ת��ʧ��");
					} else { // �����ת���Ļ�
						int i = rs.getInt("uid");
						double money1 = money * 5 / 1000;
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
							System.out.println("ת�˳ɹ�!�����ѣ�" + money1+" Ԫ");
							con.commit();
						} else { // ����Է����Ų����ڵĻ�����������
							System.out.println("��Ҫת����˻������ڣ����������룡");
							String accounts = scan.next();
							transfer(myac, money, password, accounts);
						}
					}
				}

				else {
					System.out.println("�˿�Ҳ��ʧ���ݲ�֧��ת��");
				}
			} else { // ���û�ҵ�money��yesno˵����������õݹ�ķ�����������һ��
				System.out.println("�����ʺŻ������������");
				System.out.println("�����������ʺ�");
				myac = scan.next();
				System.out.println("��������������");
				password = scan.nextInt();
				transfer(myac, money, password, heac);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Link.getInstance().closeAll(con, pst, rs);
	}
	
	// ----------------------------��ʧ
		public void miss(String ID, int password) {
			Connection con = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
			try {
				con = (Connection) Link.getInstance().getCon(); // ��������
				String sql = "select user.uid ,yesno from card,user where ID=? and password =? ";// �ҵ�uid��yesno
				pst = con.prepareStatement(sql);// ִ��sql���
				pst.setString(1, ID);
				pst.setInt(2, password); // ���������ʺ��е�����
				rs = pst.executeQuery();
				if (rs.next()) { // ����ҵ�uid��
					int i = rs.getInt("uid"); // ȡ��uid�������޸�Card�������
					String s = "update card set yesno = 'no' where uid =?";
					pst = con.prepareStatement(s);
					pst.setInt(1, i);
					pst.executeUpdate();
					System.out.println("��ʧ�ɹ�");
				} else { // ���û�ҵ�uid˵�����֤�ź����벻ƥ��
					System.out.println("������ʺŴ���\n�������������֤��");
					ID = scan.next();
					System.out.println("��������������");
					password = scan.nextInt();
					miss(ID, password); // �����������֤�ź�������ڽ��в���
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
		
		/**
		 * 
		 * �ж��������ʺ��Ƿ�ƥ�䣬�ʺ��Ƿ��ʧ
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
						System.out.println("�ÿ��ѹ�ʧ��");
						b = false;
					}
				}
				else{
					System.out.println("�ʺŻ������������������˶ԣ�");
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