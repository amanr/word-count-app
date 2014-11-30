package freecharge.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import freecharge.entity.WordInfo;

@Repository
public interface WordInfoRepository extends CrudRepository<WordInfo, String>{
	
	WordInfo findById(String id);

}
