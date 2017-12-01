package com.qdzl.bean;

import java.io.Serializable;
import java.util.*;

public class SysUser implements Serializable{
		private int id;
		private String ucode;
		private String account;
		private String password;
		private String nick;
		private String phone;
	
		public void setId(int id){
			this.id=id;
		}
		
		public int getId(){
			return this.id;
		}
		public void setUcode(String ucode){
			this.ucode=ucode;
		}
		
		public String getUcode(){
			return this.ucode;
		}
		public void setAccount(String account){
			this.account=account;
		}
		
		public String getAccount(){
			return this.account;
		}
		public void setPassword(String password){
			this.password=password;
		}
		
		public String getPassword(){
			return this.password;
		}
		public void setNick(String nick){
			this.nick=nick;
		}
		
		public String getNick(){
			return this.nick;
		}
		public void setPhone(String phone){
			this.phone=phone;
		}
		
		public String getPhone(){
			return this.phone;
		}
}