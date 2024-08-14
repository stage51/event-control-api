package javaservice.error.services;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, K> {
    final int PAGE_SIZE = 100;
    T create(K entity);
    Optional<T> read(Long id);
    T update(T entity);
    boolean delete(Long id);
    List<T> readAll();
    Page<T> readPage(int number);
}
