package com.visumIT.Business.boost.enums;

public enum Profile {
	
	ADMIN(1,"ROLE_ADMIN"),
	HR(2,"ROLE_HR"),
	PRODUCTS(3,"ROLE_PRODUCTS"),
	REPRESENTATIVE(4,"ROLE_REPRESENTATIVE");
	
	private int id;
	private String description;
	
	private Profile(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static Profile toEnum(Integer id) {
		if(id == null) {
			return null;
		}
		
		for (Profile x : Profile.values()) {
			if(id.equals(x.getId())) {
				return x;
			}
		}
		throw new IllegalArgumentException("id inválido: " + id);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
