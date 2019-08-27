package pt.darkalpha.controllers;

import java.util.ArrayList;
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
		
		List<Idea> out = new ArrayList<Idea>();

		// Reference:
		// > 'insensitive' - make query case-insensitive
		// > 'contains' - contains
		// > 'not' - does not contain
		// > 'tag' - not implemented
		
		// Search through all ideas and return the ones that have the specified keywords
		params.forEach((key, value) -> {
			ideaService.getAllIdeas().forEach(i -> {
				
				// To ease the logic below
				String v = value;
				String title = i.getTitle();
				String content = i.getContent();
				
				// Case-sensitive stuff, kind of self-explanatory.
				if(params.containsKey("insensitive")) {
					v = v.toLowerCase();
					title = title.toLowerCase();
					content = content.toLowerCase();
				}
				
				
				// Must NOT contain the specified keywords
				if(key.equals("not")) {
					if(!title.contains(v) && !content.contains(v)) {
						out.add(i);
					}
				}
				
				// Must contain the specified keywords
				if(key.equals("contains")) {
					if(title.contains(v) || content.contains(v)) {
						out.add(i);
					}
				}

				
			});
		});
		
		
		// Clean up possible duplicates because my logic in the loop is terrible
		// also the compiler complained if I used 'out' here...
		List<Idea> output = out.stream().distinct().collect(Collectors.toList());

		
		
		HttpStatus status = !output.isEmpty() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		
		return new ResponseEntity<List<Idea>>(output, status);
	}
	

}
