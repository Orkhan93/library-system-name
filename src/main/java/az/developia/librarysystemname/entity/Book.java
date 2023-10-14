package az.developia.librarysystemname.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(name = "Book.getAllProduct",
        query = "select new az.developia.librarysystemname.wrapper.BookWrapper(b.id,b.name,b.description,b.price,b.status,b.user,b.library)" +
        query = "select new az.developia.librarysystemname.wrapper.BookWrapper(b.id,b.name,b.description,b.price,b.status,b.user)" +
                " from Book b inner join User u on b.user.id=u.id and u.userRole='librarian'")

@Entity
@Data
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private String price;

    @Column(name = "status")
    private String status;

    @JsonIgnore
    @ManyToOne(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "borrow_id", referencedColumnName = "id")
    private Borrow borrow;

}