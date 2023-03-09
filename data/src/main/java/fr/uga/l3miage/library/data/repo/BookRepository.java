package fr.uga.l3miage.library.data.repo;

import fr.uga.l3miage.library.data.domain.Book;
import jakarta.persistence.EntityManager;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository implements CRUDRepository<Long, Book> {

    private final EntityManager entityManager;

    @Autowired
    public BookRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Book save(Book author) {
        entityManager.persist(author);
        return author;
    }

    @Override
    public Book get(Long id) {
        return entityManager.find(Book.class, id);
    }


    @Override
    public void delete(Book author) {
        entityManager.remove(author);
    }

    /**
     * Renvoie tous les auteurs par ordre alphabétique
     * @return une liste de livres
     */
    public List<Book> all() {
        return entityManager.createQuery("SELECT b FROM Book b ORDER BY b.title",
                                             Book.class)
                            .getResultList();
    }

    /**
     * Trouve les livres dont le titre contient la chaine passée (non sensible à la casse)
     * @param titlePart tout ou partie du titre
     * @return une liste de livres
     */
    public List<Book> findByContainingTitle(String titlePart) {
        return entityManager.createQuery("SELECT b FROM Book b WHERE LOWER(b.title) LIKE :titre ORDER BY b.title",
                                         Book.class)
                            .setParameter("titre", '%' + titlePart.toLowerCase() + '%')
                            .getResultList();
    }

    /**
     * Trouve les livres d'un auteur donnée dont le titre contient la chaine passée (non sensible à la casse)
     * @param authorId id de l'auteur
     * @param titlePart tout ou partie d'un titre de livré
     * @return une liste de livres
     */
    public List<Book> findByAuthorIdAndContainingTitle(Long authorId, String titlePart) {
        return entityManager.createQuery("SELECT b FROM Book b JOIN b.authors a WHERE LOWER(b.title) LIKE :titre AND a.id = :author ORDER BY b.title",
                                         Book.class)
                            .setParameter("titre", '%' + titlePart.toLowerCase() + '%')
                            .setParameter("author", authorId )
                            .getResultList();
    }

    /**
     * Recherche des livres dont le nom de l'auteur contient la chaine passée (non sensible à la casse)
     * @param namePart tout ou partie du nom
     * @return une liste de livres
     */
    public List<Book> findBooksByAuthorContainingName(String namePart) {
        // TODO créer les named query
        return entityManager.createQuery("SELECT b FROM Book b JOIN b.authors a WHERE LOWER (a.fullName) LIKE :authorName", Book.class)
                // TODO completer l'appel pour utiliser le paramètre de cette méthode
                .setParameter("authorName", '%' + namePart.toLowerCase() + '%')
                .getResultList();
    }

    /**
     * Trouve des livres avec un nombre d'auteurs supérieur au compte donné
     * @param count le compte minimum d'auteurs
     * @return une liste de livres
     */
    public List<Book> findBooksHavingAuthorCountGreaterThan(int count) {
        // TODO créer les named query
        return entityManager.createNamedQuery("find-books-by-several-authors", Book.class)
                // TODO completer l'appel pour utiliser le paramètre de cette méthode
                .getResultList();
    }

}
