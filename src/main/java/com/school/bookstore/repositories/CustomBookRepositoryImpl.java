package com.school.bookstore.repositories;

import com.school.bookstore.models.entities.Author;
import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.GenreTag;
import com.school.bookstore.models.enums.Language;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomBookRepositoryImpl implements CustomBookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Book> findBooksByTitleOrAuthorNameAndGenreAndLanguage(String searchString, String genre, Language language) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);

        List<Predicate> predicates = new ArrayList<>();
        Root<Book> bookRoot = criteriaQuery.from(Book.class);

        if (searchString != null && !searchString.isBlank()) {

            Predicate titlePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(bookRoot.get("title")), "%" + searchString.toLowerCase() + "%");

            Join<Book, Author> authorJoin = bookRoot.join("authors", JoinType.INNER);
            Predicate authorPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(authorJoin.get("fullName")), "%" + searchString.toLowerCase() + "%");

            predicates.add(criteriaBuilder.or(titlePredicate, authorPredicate));
        }

        if (genre != null) {
            Join<Book, GenreTag> genreJoin = bookRoot.join("genreTagSet", JoinType.INNER);
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(genreJoin.get("genre")), genre.toLowerCase()));
        }

        if (language != null) {
            predicates.add(criteriaBuilder.equal(bookRoot.get("language"), language));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Book> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }
}