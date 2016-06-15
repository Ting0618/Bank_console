package com.jr.Link;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class Link {
	private  String url="jdbc:mysql://localhost:3306/bank";
	private  String uName="root";
	private  String uPW ="123456";
	//private static SingletonTest instance = new SingletonTest();//����һ�ξʹ���һ�ζ���
	private static Link instance = null;//���ڲ�����һ������   instance��
	// ���뱻������ʵ����
	private Link(){}
	
	public static Link  getInstance(){
		if(instance==null){		//�����Ϊ�վʹ���������Ϊ�գ��Ѿ��������˾Ͳ�����������ԭ���Ǹ��������ݿ������
			synchronized(Link.class){  //�����������
				if(instance ==null){
			instance = new Link();	
				}
		}		}
		return  instance;
	}
	Connection con=null;
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  Connection getCon() throws SQLException{
		
		return DriverManager.getConnection(url,uName,uPW);
	}
	public  void closeAll(Connection con,Statement st,ResultSet rs){
		try {
		if(con!=null){		
				con.close();
			} 
		}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(st!=null){
					try {
						st.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						if(rs!=null){
							try {
								rs.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
				}
			}
		}
}
