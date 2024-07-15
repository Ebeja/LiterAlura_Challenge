package com.example.literAlura;

import com.example.literAlura.model.Author;
import com.example.literAlura.model.AuthorEntity;
import com.example.literAlura.model.Book;
import com.example.literAlura.model.BookEntity;
import com.example.literAlura.repository.BookRepository;
import com.example.literAlura.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootApplication
public class CatalogoApp {

    private static final Scanner scanner = new Scanner(System.in);
    private final GutendexService gutendexService;
    private final BookRepository bookRepository;

    public CatalogoApp(GutendexService gutendexService, BookRepository bookRepository) {
        this.gutendexService = gutendexService;
        this.bookRepository = bookRepository;
    }
    @Transactional  // Añadir la anotación @Transactional aquí
    public void start() {
        while (true) {
            System.out.println("1. Buscar libros");
            System.out.println("2. Agregar libro");
            System.out.println("3. Mostrar todos los libros");
            System.out.println("4. Filtrar por autor");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume la nueva línea

            switch (choice) {
                case 1:
                    buscarLibros();
                    break;
                case 2:
                    agregarLibro();
                    break;
                case 3:
                    mostrarTodosLosLibros();
                    break;
                case 4:
                    filtrarPorAutor();
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción inválida. Inténtelo de nuevo.");
            }
        }
    }

    @Transactional
    private void buscarLibros() {
        System.out.print("Ingrese el término de búsqueda: ");
        String query = scanner.nextLine();
        List<Book> books = gutendexService.fetchBooks(query);
        for (Book book : books) {
            System.out.println(book.getTitle() + " - " + book.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", ")));
        }
    }
    @Transactional
    private void agregarLibro() {
        System.out.print("Ingrese el título del libro: ");
        String title = scanner.nextLine();
        System.out.print("Ingrese la descripción del libro: ");
        String description = scanner.nextLine();
        System.out.print("Ingrese los autores (separados por coma): ");
        String[] authorNames = scanner.nextLine().split(",");

        List<AuthorEntity> authors = Arrays.stream(authorNames)
                .map(name -> findOrCreateAuthor(name.trim()))
                .collect(Collectors.toList());

        BookEntity book = new BookEntity();
        book.setTitle(title);
        book.setDescription(description);
        book.setAuthors(authors);

        bookRepository.save(book);
        System.out.println("Libro agregado exitosamente.");
    }
    @Transactional
    private void mostrarTodosLosLibros() {
        List<BookEntity> books = bookRepository.findAllWithAuthors();
        for (BookEntity book : books) {
            System.out.println(book.getTitle() + " - " + book.getAuthors().stream().map(AuthorEntity::getName).collect(Collectors.joining(", ")));
        }
    }

    @Transactional
    private void filtrarPorAutor() {
        System.out.print("Ingrese el nombre del autor: ");
        String authorName = scanner.nextLine();
        List<BookEntity> books = bookRepository.findAll().stream()
                .filter(book -> book.getAuthors().stream().anyMatch(author -> author.getName().equalsIgnoreCase(authorName)))
                .collect(Collectors.toList());
        for (BookEntity book : books) {
            System.out.println(book.getTitle() + " - " + book.getAuthors().stream().map(AuthorEntity::getName).collect(Collectors.joining(", ")));
        }
    }


    private AuthorEntity findOrCreateAuthor(String name) {
        AuthorEntity author = authorRepository.findByNameIgnoreCase(name);
        if (author == null) {
            author = new AuthorEntity(name);
            authorRepository.save(author);
        }
        return author;
    }


    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CatalogoApp.class, args);
        CatalogoApp app = context.getBean(CatalogoApp.class);
        app.start();
    }
}
