package pt.darkalpha.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.darkalpha.models.Idea;
import pt.darkalpha.services.IdeaService;

@RestController
@RequestMapping("/api/ideas")
public class RestIdeaController {
	
	@Autowired
	IdeaService ideaService;
	
	
	
	@PostMapping("")
	public ResponseEntity<Idea> postIdea(
			@RequestParam("title") String title,
			@RequestParam("content") String content) {
		return new ResponseEntity<Idea>(ideaService.saveIdea(new Idea(title, content)), HttpStatus.CREATED);
	}
	
	
	@GetMapping("")
	public ResponseEntity<Idea> getIdeaById(@RequestParam(value="id", defaultValue="0") Long id) {
		
		
		
		Idea found = ideaService.getIdeaById(id);
		
		// Is this more readable?
		//return found != null ? new ResponseEntity<Idea>(found, HttpStatus.OK) : new ResponseEntity<Idea>(HttpStatus.NOT_FOUND);
		
		if(found != null) {
			return new ResponseEntity<Idea>(found, HttpStatus.OK);
			
		} else {
			return new ResponseEntity<Idea>(HttpStatus.NOT_FOUND);
		}
		

	}
	
	@GetMapping("/random")
	public ResponseEntity<Idea> getRandomIdea() {
		List<Idea> all = ideaService.getAllIdeas();
		
		if(all.isEmpty()) {
			// Couldn't find any status relevant to this case...
			return new ResponseEntity<Idea>(HttpStatus.NOT_FOUND);
		} else {
			
			int index = (int) Math.round(Math.random() * all.size()) - 1;
			index = index > 0 ? index : index + 1;
			
			
			return new ResponseEntity<Idea>(ideaService.getAllIdeas().get(index), HttpStatus.OK);
		}

		
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Idea>> getAllIdeas() {
		return new ResponseEntity<List<Idea>>(ideaService.getAllIdeas(), HttpStatus.OK);

		
	}
	
	@DeleteMapping("")
	public HttpStatus deleteIdea(@RequestParam(value="id", defaultValue="0") Long id) {
		ideaService.deleteIdea(id);
		return HttpStatus.OK;
	}
	
//	@GetMapping("/test")
//	public List<String> logParams(@RequestParam Map<String, String> params) {
//		List<String> out = new ArrayList<String>();
//		
//		params.forEach((key, value) -> {
//			System.out.println(">>> Ran");
//			out.add(key + " - " + value);
//		});
//		
//		return out;
//	}
	
	public ResponseEntity<List<Idea>> findByTitle(List<String> whitelist, List<String> blacklist, boolean caseSensitive, int maxResults) {
		@SuppressWarnings("unused")
		String query = "FROM Idea i WHERE title in :must AND title";
		
		
		return null;
	}
	
	@GetMapping("/find")
	public ResponseEntity<List<Idea>> find(@RequestParam Map<String, String> params) {
		
		// Reference:
		// > 'insensitive' - make query case-insensitive
		// > 'contains' - contains
		// > 'not' - does not contain
		// > 'tag' - not implemented
		
		// If any of the parameters is "insensitive", then the whole
		// query case-insensitive
		
		
		List<Idea> out = ideaService.getAllIdeas();

		System.out.println(params.toString());
		
		// This much duplicate code is making my eyes cry
		// please send help, I couldn't figure out any better method than the previous...
		
		String[] whitelist = params.get("contains") != null
				? params.get("contains").split(",") : new String[0];
		
		String[] blacklist = params.get("not") != null
				? params.get("not").split(",") : new String[0];
		
		
		if(params.containsKey("insensitive")) {
			
			for(String word : whitelist) {
				out = out.stream().filter(
						x -> x.getTitle().concat(x.getContent()).toLowerCase().contains(word.toLowerCase().strip())
						).collect(Collectors.toList());
			}
			
			for(String word : blacklist) {
				out = out.stream().filter(
						x -> !x.getTitle().concat(x.getContent()).toLowerCase().contains(word.toLowerCase().strip())
						).collect(Collectors.toList());
			}
			
			
			
			
		} else {
			
			for(String word : whitelist) {
				out = out.stream().filter(
						x -> x.getTitle().concat(x.getContent()).contains(word)
						).collect(Collectors.toList());
			}
			
			for(String word : blacklist) {
				out = out.stream().filter(
						x -> !x.getTitle().concat(x.getContent()).contains(word)
						).collect(Collectors.toList());
			}
			
		}
		
		
		// Clean up possible duplicates because my logic in the loop is terrible
		// also the compiler complained if I used 'out' here...
		
		// ->>> shouldn't be needed anymore... I HOPE
		//List<Idea> output = out.stream().distinct().collect(Collectors.toList());

		
		
		HttpStatus status = !out.isEmpty() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		
		return new ResponseEntity<List<Idea>>(out, status);
	}
	

}
