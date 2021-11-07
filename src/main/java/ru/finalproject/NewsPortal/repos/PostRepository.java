package ru.finalproject.NewsPortal.repos;

import ru.finalproject.NewsPortal.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

}
