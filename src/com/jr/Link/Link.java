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
	//private static SingletonTest instance = new SingletonTest();//加载一次就创建一次对象
	private static Link instance = null;//在内部创建一个对象   instance？
	// 不想被其他人实例化
	private Link(){}
	
	public static Link  getInstance(){
		if(instance==null){		//如果它为空就创建，若不为空（已经创建过了就不创建）就用原来那个进行数据库的连接
			synchronized(Link.class){  //琐的是这个类
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
