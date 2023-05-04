package spring.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.demo.model.Book;
import spring.demo.repo.BookRepository;

/**
 * Hello world!
 *
 */
@RestController
@RequestMapping("/book")
public class App 
{
	@Autowired
	private BookRepository bookrepo;
	
	@PostMapping
	public Book saveBook(@RequestBody Book book)
	{
		return bookrepo.save(book);	
	}
	public List<Book> getAllBooks()
	{
		return bookrepo.findAll();
		
	}
	
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
