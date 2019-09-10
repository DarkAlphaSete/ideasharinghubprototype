package pt.darkalpha.services;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pt.darkalpha.models.Idea;
import pt.darkalpha.repositories.IdeaRepository;

@Service
public class IdeaService {
	
	@Autowired
	IdeaRepository ideaRepository;
	
	
	
	public Idea postIdea(Idea idea) {
		return ideaRepository.save(idea);
	}
	public Idea postIdea(String title, String[] tags) {
		return postIdea(new Idea(title, tags));
	}
	
	public boolean removeIdea(Long id) {
		if(ideaRepository.findById(id).isPresent()) {
			ideaRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
		
	}
	public void removeIdea(Idea idea) {
		removeIdea(idea.getId());
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
	
	public Page<Idea> getAllIdeas(Pageable pageable) {
		return ideaRepository.findAll(pageable);
	}
	
	
	public Page<Idea> findByKeyword(String keyword, boolean caseSensitive, Pageable pageable) {
		return caseSensitive
				? ideaRepository.findByTitleContaining(keyword, pageable)
				: ideaRepository.findByTitleContainingIgnoreCase(keyword, pageable);
	}
	
	public Page<Idea> findWithoutKeyword(String keyword, boolean caseSensitive, Pageable pageable) {
		return caseSensitive
				? ideaRepository.findByTitleNotContaining(keyword, pageable)
				: ideaRepository.findByTitleNotContainingIgnoreCase(keyword, pageable);
	}
	
	
	// TODO: Make this work with Pages
	public List<Idea> filterByKeywords(List<Idea> list, List<String> blacklist, boolean caseSensitive) {
		
		if(caseSensitive) {
			for(String word : blacklist) {
				list = list.stream().filter(
						x -> !x.getTitle().concat(x.getTitle()).contains(word.strip())
						).collect(Collectors.toList());
			}
		} else {
			for(String word : blacklist) {
				list = list.stream().filter(
						x -> !x.getTitle().concat(x.getTitle()).toLowerCase().contains(word.toLowerCase().strip())
						).collect(Collectors.toList());
			}
		}
		
		return list;
	}
	

}
