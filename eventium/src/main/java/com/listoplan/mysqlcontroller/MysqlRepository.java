package com.listoplan.mysqlcontroller;


public class MysqlRepository {
	private static MysqlRepository mysqlRepository=null;
	private  MysqlManager mr;
	
	private MysqlRepository(){
		this.mr= MysqlManager.getInstance();
		this.mr.connect();
	};
	
	public static MysqlRepository getInstance(){
		if (mysqlRepository==null){
			mysqlRepository= new MysqlRepository();
		}
		return mysqlRepository;
	}
	
	
	public void disconnect(){
		this.mr.quit();
	}
}
