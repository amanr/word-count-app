package freecharge.entity;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class WordInfo {
	
	@Id
	private String id;
	private int count;
	
	public WordInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WordInfo(String word, int count) {
		super();
		this.id = word;
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}	

}
