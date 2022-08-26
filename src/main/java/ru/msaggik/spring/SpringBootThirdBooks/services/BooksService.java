package ru.msaggik.spring.SpringBootThirdBooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msaggik.spring.SpringBootThirdBooks.models.Book;
import ru.msaggik.spring.SpringBootThirdBooks.models.Person;
import ru.msaggik.spring.SpringBootThirdBooks.repositories.BooksRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    // сортировка книг по году
    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }
    // сортировка книг
    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year")))
                    .getContent(); // .getContent() - выдача в виде списка
        else
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }
    // демонстрация выбранной книги
    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }
    // метод поиска книг по начальным буквам в названии книги (см. репозиторий книги)
    public List<Book> searchByTitle(String query) {
        return booksRepository.findByTitleStartingWith(query);
    }

    // сохранение книги
    @Transactional
    public void save(Book book) {
        book.setTakenAt(new Date()); // автоматическое задание даты
        booksRepository.save(book);}

    // обновление данных книги
    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = booksRepository.findById(id).get();
        // добавление новой книги, которая не находится в Persistence context, следовательно нужен save()
        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner()); // чтобы не потерялась связь при обновлении
        booksRepository.save(updatedBook);
    }
    // удаление книги
    @Transactional
    public void delete(int id) { booksRepository.deleteById(id);}

    // получение данных владельца книги, если владелец не найден то возвращается null
    public Person getBookOwner(int id) {
        // здесь Hibernate.initialize() не нужен, так как владелец (сторона One) загружается не лениво
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    // возврат книги от пользователя обратно в библиотеку
    @Transactional
    public void release(int id) {
        booksRepository.findById(id).ifPresent(
                book -> { // здесь книга находится в Persistence context, следовательно можно обойтись сеттером
                    book.setOwner(null);
                    book.setTakenAt(null);
                });
    }

    // получение пользователем книги в библиотеке
    @Transactional
    public void assign(int id, Person selectedPerson) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    book.setTakenAt(new Date()); // текущее время
                });
    }
}
