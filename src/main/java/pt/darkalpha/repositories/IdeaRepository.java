package pt.darkalpha.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.darkalpha.models.Idea;

@Repository
public interface IdeaRepository extends CrudRepository<Idea, Long> {

}
