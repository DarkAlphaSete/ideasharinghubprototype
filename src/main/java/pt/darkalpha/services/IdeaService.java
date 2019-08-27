package pt.darkalpha.services;


import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.darkalpha.models.Idea;
import pt.darkalpha.repositories.IdeaRepository;

@Service
public class IdeaService {
	
	@Autowired
	IdeaRepository ideaRepository;
	
	
	
	public Idea saveIdea(Idea idea) {
		return ideaRepository.save(idea);
	}
	public Idea saveIdea(String title, String content) {
		return saveIdea(new Idea(title, content));
	}
	
	public void deleteIdea(Long id) {
		ideaRepository.deleteById(id);
		
	}
	public void deleteIdea(Idea idea) {
		deleteIdea(idea.getId());
	}
	
	public Idea getIdeaById(Long id) {
				
		try {
			return ideaRepository.findById(id).get();
			
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	
	public List<Idea> getAllIdeas() {
		return (List<Idea>) ideaRepository.findAll();
	}
	

}
