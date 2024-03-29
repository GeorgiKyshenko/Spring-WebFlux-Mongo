import com.dxc.library.webflux.dto.BookDto;
import com.dxc.library.webflux.entity.Book;
import com.dxc.library.webflux.mapper.BookMapper;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {

        Book book = Book.builder().isbn("123").name("name").authorName("author").pages(200).build();

        BookDto bookDto = BookMapper.FromBookToBookDtoOrBackwards(book, BookDto.class);

        System.out.println(bookDto);

        Book book1 = BookMapper.FromBookToBookDtoOrBackwards(bookDto, Book.class);
        System.out.println(book1);
    }
}
