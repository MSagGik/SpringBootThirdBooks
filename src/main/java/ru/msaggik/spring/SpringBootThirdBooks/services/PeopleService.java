package ru.msaggik.spring.SpringBootThirdBooks.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msaggik.spring.SpringBootThirdBooks.models.Book;
import ru.msaggik.spring.SpringBootThirdBooks.models.Person;
import ru.msaggik.spring.SpringBootThirdBooks.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true) // внутри класса режим чтения можно изменять на обычный режим изменения
public class PeopleService {


    // внедрение репозитория
    private final PeopleRepository peopleRepository;
    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    // метод findAll() возвращает все сущности из таблицы БД
    public List<Person> findAll() {
        return peopleRepository.findAll();
    }
    // возврат из БД данных одного пользователя по его id
    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);

        return foundPerson.orElse(null); // если запрашиваемых данных нет, то возвращается null
    }

    // сохранение данных в БД
    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }
    // обновление данных в БД
    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }
    // удаление из БД пользователя по id
    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    // поиска книги по её названию или первой части названия
    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    // проверка просроченных книг
    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id); // получение пользователя по id
        if (person.isPresent()) { // если пользователь в БД есть, то:
            Hibernate.initialize(person.get().getBooks()); // получение всех книг для пользователя
            // далее идёт итерация по книгам, поэтому они будут загружены, тем не менее
            // на непредвиденный случай нужно вызывать Hibernate.initialize()
            // (на случай, например, если код в дальнейшем изменится и итерация по книгам удалится)

            // проверка просроченности книг
            person.get().getBooks().forEach(book -> {
                long diffInMilles = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                // Math.abs() - модуль числа, 864'000'000 = 10 суток
                if(diffInMilles > 864_000_000)
                    book.setExpired(true); // книга просрочена
            });
            return person.get().getBooks();
        }
        else { // иначе если пользователя в БД нет, то выводится пустой список
            return Collections.emptyList();
        }
    }
}
