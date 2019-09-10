package pt.darkalpha.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Idea {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private Long timePosted;
	

	private Long stars;
	private String title;
	private String[] tags;
	
	public Idea() {
		timePosted = System.currentTimeMillis();
		
	}
	
	public Idea(String title, String[] tags) {
		this.stars = 0L;
		this.title = title;
		this.tags = lowerArray(tags);

		timePosted = System.currentTimeMillis();
	}
	
	
	public Long getStars() {
		return stars;
	}
	public void setStars(Long stars) {
		this.stars = stars;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = lowerArray(tags);
	}
	public Long getId() {
		return id;
	}
	public Long getTimePosted() {
		return timePosted;
	}

	
	
	private String[] lowerArray(String[] array) {
		
		String[] out = new String[array.length];
		
		for(int i = 0; i < array.length; i++) {
			out[i] = array[i].toLowerCase();
		}
		
		return out;
		
	}
	
	// It's only the same thing if the ID is the same
	@Override
	public boolean equals(Object other) {
		if(other == null || !other.getClass().isAssignableFrom(Idea.class)) {
			return false;
		}

		final Idea idea = (Idea) other;
		
		return idea.getId() == this.getId();
	}

	
}
