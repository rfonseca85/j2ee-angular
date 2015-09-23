package ca.rfonseca.j2eeangular.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import ca.rfonseca.j2eeangular.model.Book;
import ca.rfonseca.j2eeangular.pagination.PaginationList;

/**
 * REST Service to expose the data.
 *
 * @author Rafael Fonseca
 */
@Stateless
@ApplicationPath("/resources")
@Path("books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookResource extends Application {
    @PersistenceContext
    private EntityManager entityManager;

    private Integer countBooks() {
        Query query = entityManager.createQuery("SELECT COUNT(p.id) FROM Book p");
        return ((Long) query.getSingleResult()).intValue();
    }

    @SuppressWarnings("unchecked")
    private List<Book> findBooks(int startPosition, int maxResults, String sortFields, String sortDirections) {
        Query query =
                entityManager.createQuery("SELECT p FROM Book p ORDER BY p." + sortFields + " " + sortDirections);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    private PaginationList findBooks(PaginationList wrapper) {
        wrapper.setTotalResults(countBooks());
        int start = (wrapper.getCurrentPage() - 1) * wrapper.getPageSize();
        wrapper.setList(findBooks(start,
                                    wrapper.getPageSize(),
                                    wrapper.getSortFields(),
                                    wrapper.getSortDirections()));
        return wrapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PaginationList listBooks(@DefaultValue("1")
                                            @QueryParam("page")
                                            Integer page,
                                            @DefaultValue("id")
                                            @QueryParam("sortFields")
                                            String sortFields,
                                            @DefaultValue("asc")
                                            @QueryParam("sortDirections")
                                            String sortDirections) {
        PaginationList paginatedListWrapper = new PaginationList();
        paginatedListWrapper.setCurrentPage(page);
        paginatedListWrapper.setSortFields(sortFields);
        paginatedListWrapper.setSortDirections(sortDirections);
        paginatedListWrapper.setPageSize(10);
        return findBooks(paginatedListWrapper);
    }

    @GET
    @Path("{id}")
    public Book getBook(@PathParam("id") Long id) {
        return entityManager.find(Book.class, id);
    }

    @POST
    public Book saveBook(Book book) {
        if (book.getId() == null) {
            Book bookToSave = new Book();
            bookToSave.setName(book.getName());
            bookToSave.setDescription(book.getDescription());
            bookToSave.setImageUrl(book.getImageUrl());
            entityManager.persist(book);
        } else {
            Book bookToUpdate = getBook(book.getId());
            bookToUpdate.setName(book.getName());
            bookToUpdate.setDescription(book.getDescription());
            bookToUpdate.setImageUrl(book.getImageUrl());
            book = entityManager.merge(bookToUpdate);
        }

        return book;
    }

    @DELETE
    @Path("{id}")
    public void deleteBook(@PathParam("id") Long id) {
        entityManager.remove(getBook(id));
    }
}
