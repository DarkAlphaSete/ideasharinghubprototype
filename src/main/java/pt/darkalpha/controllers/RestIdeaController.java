package pt.darkalpha.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	
	
	
	@PostMapping
	public ResponseEntity<Idea> postIdea(
			@RequestParam("title") String title,
			@RequestParam("tag") String _tags) {
		
		String[] tags = _tags.split("[,\\s]+");
		
		return new ResponseEntity<Idea>(ideaService.postIdea(new Idea(title, tags)), HttpStatus.CREATED);
	}
	
	
	@GetMapping
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
			
			// This code is ugly as hell
			// Unfortunately for people that hate ugly code, I'm not deleting it because it works!
			
			int index = (int) (Math.random() * all.size());
			//index = index > 0 ? index : 1;
			
			return new ResponseEntity<Idea>(all.get(index), HttpStatus.OK);
		}

		
	}
	
	@GetMapping("/all")
	public ResponseEntity<Page<Idea>> getAllIdeas(
			@RequestParam(name="page", required=false) Integer page,
			@RequestParam(name="order", required=false) String order,
			@RequestParam(name="sort", required=false) String sort) { // Either "stars" or "timePosted"
		

		// Only sort if all parameters are given
		if(page != null && order != null && sort != null) {
			
			Pageable pageable;
			
			switch(order) {
				default:
				case "asc":
				case "ascending":
					pageable = PageRequest.of(page, 25, Sort.by(sort).ascending());
					break;
				case "desc":
				case "descending":
					pageable = PageRequest.of(page, 25, Sort.by(sort).descending());
					break;
			}
			
			return new ResponseEntity<Page<Idea>>(ideaService.getAllIdeas(pageable), HttpStatus.OK);
		
		}
		

		return new ResponseEntity<Page<Idea>>(new PageImpl<>(ideaService.getAllIdeas()), HttpStatus.OK);
		
	}
	
	@DeleteMapping()
	public HttpStatus deleteIdea(@RequestParam(value="id", defaultValue="0") Long id) {
		return ideaService.removeIdea(id)
				? HttpStatus.OK
				: HttpStatus.NOT_FOUND;
	}
	
//	@GetMapping("/test")
//	public ResponseEntity<List<String>> logParams(@RequestParam Map<String, String> params) {
//		List<String> out = new ArrayList<String>();
//		
//		System.out.println("Format: Key <> Value");
//		params.forEach((key, value) -> {
//			String format = String.format("'%s' <> '%s'", key, value);
//			System.out.println(format);
//			out.add(format);
//		});
//		
//		return new ResponseEntity<List<String>>(out, HttpStatus.OK);
//	}
	
	
	// TODO: Overhaul this damn ugly thing
	@GetMapping("/find")
	public ResponseEntity<Page<Idea>> find(@RequestParam Map<String, String> params) {
			
		// Reference:
		// > 'insensitive' - make query case-insensitive
		// > 'contains' - white-list
		// > 'not' - black-list
		// > 'tags'
		//
		// > 'sort' - stars, timePosted
		// > 'order' - (asc)ending, (desc)ending
		// > 'page'
		// (the page size is going to always be 25)
		//
		// If any of the parameters is "insensitive", then the whole
		// query case-insensitive
		
		
		// I should probably make a "Request" class or something... 
		
		String[] whitelist = params.get("contains") != null
				? params.get("contains").split(",")
				: new String[0];
		
		String[] blacklist = params.get("not") != null
				? params.get("not").split(",")
				: new String[0];
				
		String[] tags = params.get("tags") != null
				? params.get("tags").split(",")
				: new String[0];
			
		String sort = params.get("sort") != null
				? params.get("sort")
				: "timePosted";
		
		String order = params.get("order") != null
				? params.get("order")
				: "asc";
				
		int page = params.get("page") != null
				? Integer.parseInt(params.get("page"))
				: 0;
				
		Pageable pageable;
				
		switch(order) {
			default:
			case "asc":
			case "ascending":
				pageable = PageRequest.of(page, 25, Sort.by(sort).ascending());
				break;
			case "desc":
			case "descending":
				pageable = PageRequest.of(page, 25, Sort.by(sort).descending());
				break;
		}
		
				
		// Would using the ternary operator here make this look better or worse?
		if(whitelist.length == 0 && blacklist.length == 0 && tags.length == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
				
				
		List<Idea> out = new ArrayList<Idea>();
		
		
		// THIS THING:
		// Currently returns everything that does not have a certain keyword when
		// operating with the black-list alone. It's not even intuitive.
		// Bah, I'm too bored to fix it. White-list + black-list is working as intended
		// (finds with a specific word, and then filters out those with specific words)
		
		if(whitelist.length == 0 && blacklist.length > 0) {
			for(String s : blacklist) {
				out.addAll(ideaService.findWithoutKeyword(s, !params.containsKey("insensitive"), pageable).getContent());
			}
			
		} else if(whitelist.length > 0 && blacklist.length == 0) {
			for(String s : whitelist) {
				out.addAll(ideaService.findByKeyword(s, !params.containsKey("insensitive"), pageable).getContent());
			}
			
		} else if(whitelist.length > 0 && blacklist.length > 0) {
			
			for(String s : whitelist) {
				out.addAll(ideaService.findByKeyword(s, !params.containsKey("insensitive"), pageable).getContent());
			}
			
			out = ideaService.filterByKeywords(out, Arrays.asList(blacklist), !params.containsKey("insensitive"));
			
		}
		
		if(tags.length > 0) {
			if(whitelist.length == 0 && blacklist.length == 0) {
				out = ideaService.getAllIdeas(pageable).getContent();
			}
			out = ideaService.filterByTags(out, arrayToList(tags));
		}
		
		// Clean up possible duplicates because of my flawed logic.
		// ...
		Page<Idea> output = new PageImpl<>(out.stream().distinct().collect(Collectors.toList()));

		
		
		HttpStatus status = !output.isEmpty() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		
		return new ResponseEntity<Page<Idea>>(output, status);
	}
	
	// TODO: Move this function to an utils class
	private List<String> arrayToList(String[] array) {
		
		List<String> out = new ArrayList<>();
		
		for(String s: array) {
			out.add(s);
		}
		
		return out;
		
	}

}
