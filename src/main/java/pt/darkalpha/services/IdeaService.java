package pt.darkalpha.services;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
	
	public boolean deleteIdea(Long id) {
		if(ideaRepository.findById(id).isPresent()) {
			ideaRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
		
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
	
	
	public List<Idea> findByKeyword(String keyword, boolean caseSensitive) {
		return caseSensitive
				? ideaRepository.findByTitleOrContentContainingKeyword(keyword)
				: ideaRepository.findByTitleOrContentContainingKeywordIgnoreCase(keyword);
	}
	
	public List<Idea> findWithoutKeyword(String keyword, boolean caseSensitive) {
		return caseSensitive
				? ideaRepository.findByTitleOrContentWithoutContainingKeyword(keyword)
				: ideaRepository.findByTitleOrContentWithoutContainingKeywordIgnoreCase(keyword);
	}
	
	public List<Idea> filterByKeywords(List<Idea> list, List<String> blacklist, boolean caseSensitive) {
		
		if(caseSensitive) {
			for(String word : blacklist) {
				list = list.stream().filter(
						x -> !x.getTitle().concat(x.getContent()).contains(word.strip())
						).collect(Collectors.toList());
			}
		} else {
			for(String word : blacklist) {
				list = list.stream().filter(
						x -> !x.getTitle().concat(x.getContent()).toLowerCase().contains(word.toLowerCase().strip())
						).collect(Collectors.toList());
			}
		}
		
		return list;
	}
	

}
