package freecharge.restcontroller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import freecharge.entity.WordInfo;
import freecharge.repos.WordInfoRepository;
import freecharge.restcontroller.helper.WordCountHelper;
import freecharge.utils.AppConstants;
import freecharge.utils.WordCache;
import freecharge.wso.WordCountWSO;

@RestController
@RequestMapping("/word")
public class WordController {
	
	@Autowired
	private WordCountHelper wordCountHelper;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private WordInfoRepository wordInfoRepository;
	
	private WordCache cache;
	
	@PostConstruct
	public void initialize() throws Exception{
		try{
			wordCountHelper.prepareWordData(env.getProperty(AppConstants.INPUT_FILES_PATH));
			cache = new WordCache(Integer.valueOf(env.getProperty(AppConstants.CACHE_SIZE)));
		}catch(Exception e){
			throw new Exception("Could not initialize application----> data loading failed");
		}
		
	}

	@RequestMapping("/count")
	public WordCountWSO getCount(@RequestParam(required=true, value="query") String queryWord){
		WordCountWSO wso = cache.get(queryWord);
		if(wso != null){
			System.out.println("found :"+queryWord+" in cache");
			return wso;
		}
		WordInfo info = wordInfoRepository.findById(queryWord);
		if(info != null){
			wso = new WordCountWSO(queryWord, info.getCount());
		}else {
			wso = new WordCountWSO(queryWord, 0);
		}
		cache.put(queryWord, wso);
		return wso;
	}
}
