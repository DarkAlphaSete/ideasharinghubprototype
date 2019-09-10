package pt.darkalpha.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import pt.darkalpha.models.Idea;

@Repository
public interface IdeaRepository extends PagingAndSortingRepository<Idea, Long> {
	
	// Didn't want to delete these so I guess they stay're here
	//@Query("FROM Idea i WHERE i.title LIKE CONCAT('%', :keyword, '%')")
	//@Query("FROM Idea i WHERE LOWER(i.title) LIKE (CONCAT('%', LOWER(:keyword), '%'))")
	
	Page<Idea> findByTitleContaining(String keyword, Pageable pageable);
	Page<Idea> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
	
	@Query("FROM Idea i WHERE i.title NOT LIKE CONCAT('%', :keyword, '%')")
	Page<Idea> findByTitleNotContaining(String keyword, Pageable pageable);
	@Query("FROM Idea i WHERE LOWER(i.title) NOT LIKE (CONCAT('%', LOWER(:keyword), '%'))")
	Page<Idea> findByTitleNotContainingIgnoreCase(String keyword, Pageable pageable);


	
	@Query("FROM Idea i WHERE LOWER(:tag) IN i.tags")
	Page<Idea> findByTag(String tag, Pageable pageable);
	

}
