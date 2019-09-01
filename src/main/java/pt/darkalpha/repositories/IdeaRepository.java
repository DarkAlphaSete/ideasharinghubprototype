package pt.darkalpha.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.darkalpha.models.Idea;

@Repository
public interface IdeaRepository extends CrudRepository<Idea, Long> {
	
	@Query("FROM Idea i WHERE CONCAT(i.title, i.content) LIKE CONCAT('%', :keyword, '%')")
	List<Idea> findByTitleOrContentContainingKeyword(String keyword);
	@Query("FROM Idea i WHERE LOWER(CONCAT(i.title, i.content)) LIKE (CONCAT('%', LOWER(:keyword), '%'))")
	List<Idea> findByTitleOrContentContainingKeywordIgnoreCase(String keyword);
	
	
	@Query("FROM Idea i WHERE CONCAT(i.title, i.content) NOT LIKE CONCAT('%', :keyword, '%')")
	List<Idea> findByTitleOrContentWithoutContainingKeyword(String keyword);
	@Query("FROM Idea i WHERE LOWER(CONCAT(i.title, i.content)) NOT LIKE (CONCAT('%', LOWER(:keyword), '%'))")
	List<Idea> findByTitleOrContentWithoutContainingKeywordIgnoreCase(String keyword);

}
