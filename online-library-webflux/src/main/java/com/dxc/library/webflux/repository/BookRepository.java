package com.dxc.library.webflux.repository;

import com.dxc.library.webflux.entity.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/*we can use also extends ReactiveMongoRepository it is the same because it extends ReactiveCrudRepository, but for now
* we need methods only from ReactiveCrudRepository*/
public interface BookRepository extends ReactiveCrudRepository<Book, String> {
}
