package freecharge.restcontroller.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import freecharge.entity.WordInfo;
import freecharge.repos.WordInfoRepository;
import freecharge.utils.AppConstants;

@Component
public class WordCountHelper {
	
	@Autowired
	private WordInfoRepository wordInfoRepository;
	
	@Autowired
	private Environment env;
		
	public void prepareWordData(String directory) throws IOException{
		File dir = new File(directory);
		Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
		int maxSize = Integer.valueOf(env.getProperty(AppConstants.MAX_LENGTH_OF_DATA_To_PERSIST));
		if(dir.exists() && dir.isDirectory()){
			for(String f : dir.list()){
				readFileAndCreateData(directory+ File.separator +f, wordCountMap, maxSize);
			}
		}else if(dir.isFile()){
			readFileAndCreateData(directory, wordCountMap, maxSize);
		}
		persistWordInfo(wordCountMap);
	}
	
	private void readFileAndCreateData(String file, Map<String, Integer> wordCountMap, int maxSize) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String str = null;
		while((str = br.readLine()) != null){
			StringTokenizer tokenizer = new StringTokenizer(str);
			while(tokenizer.hasMoreTokens()){
				String token = tokenizer.nextToken();
				if(wordCountMap.get(token) == null){
					wordCountMap.put(token, 1);
				}else{
					wordCountMap.put(token, wordCountMap.get(token)+1);
				}
			}
			if(wordCountMap.size() == maxSize){
				persistWordInfo(wordCountMap);
				wordCountMap.clear();
			}
		}
		br.close();
	}
	
	private void persistWordInfo(Map<String, Integer> wordCountMap){
		List<WordInfo> list = new ArrayList<WordInfo>(wordCountMap.size());
		for(String key : wordCountMap.keySet()){
			WordInfo info = wordInfoRepository.findById(key);
			if(info != null){
				info.setCount(info.getCount()+wordCountMap.get(key));
			}else{
				info = new WordInfo(key, wordCountMap.get(key));
			}
			list.add(info);
		}
		wordInfoRepository.save(list);
	}
	
	

}
