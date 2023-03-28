package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@Controller
@RequestMapping("/books")
public class BookController {
    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        addSampleBooks(bookRepository);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("books", bookRepository.findAll());

        return "books/list";
    }

    @GetMapping("/new")
    public String getAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/form";
    }

    @PostMapping
    public String addBook(@Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/form";
        }
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") UUID bookId, Model model) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            return "not-found";
        }
        model.addAttribute("book", book.get());
        return "books/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            return "not-found";
        }
        bookRepository.delete(book.get());
        return "redirect:/books";
    }


    private void addSampleBooks(BookRepository bookRepository) {
        Book book1 = new Book();
        book1.setTitle("First book");
        book1.setPublisher("Publisher");
        book1.setIsbn("11111-1111");
        book1.setAuthor("John");
        bookRepository.save(book1);


        Book book2 = new Book();
        book2.setTitle("Second book");
        book2.setPublisher("Publisher");
        book2.setIsbn("222202220-2");
        book2.setAuthor("Peter");

        bookRepository.save(book2);
    }
}
