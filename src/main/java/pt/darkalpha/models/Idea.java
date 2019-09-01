package pt.darkalpha.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Idea {
	
	@Id
	@GeneratedValue
	private Long id;
	

	private Long stars;
	private String title;
	private String content;
	
	public Idea() {
		
	}
	
	public Idea(String title, String content) {
		this.stars = 0L;
		this.title = title;
		this.content = content;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getId() {
		return id;
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
