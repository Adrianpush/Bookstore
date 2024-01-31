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
    private final int ENTRIES_PER_PAGE = 5;

    @Override
    public List<Book> findBooksByTitleOrAuthorNameAndGenreAndLanguage(
            String searchString, String genre, Language language, int pageNumber
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);

        Root<Book> bookRoot = criteriaQuery.from(Book.class);
        criteriaQuery.orderBy(criteriaBuilder.asc(bookRoot.get("updatedAt")));
        List<Predicate> predicates = new ArrayList<>();

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

        int firstResult = (pageNumber - 1) * ENTRIES_PER_PAGE;

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Book> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery
                .setFirstResult(firstResult)
                .setMaxResults(ENTRIES_PER_PAGE)
                .getResultList();
    }
}