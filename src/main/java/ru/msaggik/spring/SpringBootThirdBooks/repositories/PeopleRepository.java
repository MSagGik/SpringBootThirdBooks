package ru.msaggik.spring.SpringBootThirdBooks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.msaggik.spring.SpringBootThirdBooks.models.Person;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    // кастомные методы:
//    // 1) поиск пользователей по имени
//    List<Person> findByName(String name);
//    // 2) полученные значения имён сортировать по возрасту
//    List<Person> findByNameOrderByAge(String name);
//    // 3) поиск пользователей по email
//    List<Person> findByEmail(String email);
//    // 4) поиск пользователей по первым буквам имени
//    List<Person> findByNameStartingWith(String startingWith);
//    // 5) поиск пользователей по имени или по email
//    List<Person> findByNameOrEmail(String name, String email);
    // 6) данный метод нужен для валидатора
    Optional<Person> findByFullName(String fullName);
}
