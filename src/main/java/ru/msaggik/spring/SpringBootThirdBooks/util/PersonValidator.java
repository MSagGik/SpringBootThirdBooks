package ru.msaggik.spring.SpringBootThirdBooks.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.msaggik.spring.SpringBootThirdBooks.models.Person;
import ru.msaggik.spring.SpringBootThirdBooks.services.PeopleService;

@Component
public class PersonValidator implements Validator {


    // обращение к БД
//    private final PersonDAO personDAO;
        private final PeopleService peopleService;

//    @Autowired
//    public PersonValidator(PersonDAO personDAO) {
//        this.personDAO = personDAO;
//    }

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

//    @Override
//    public void validate(Object o, Errors errors) {
//        Person person = (Person) o; // даункастинг объекта о до поля person
//        // мониторинг повторения ФИО в БД
//        if (personDAO.getPersonByFullName(person.getFullName()).isPresent())
//            // errors.rejectValue(поле, код ошибки, сообщение ошибки)
//            errors.rejectValue("fullName", "", "Человек с таким ФИО уже существует");
//    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o; // даункастинг объекта о до поля person
        // мониторинг повторения ФИО в БД
        if (peopleService.getPersonByFullName(person.getFullName()).isPresent())
            // errors.rejectValue(поле, код ошибки, сообщение ошибки)
            errors.rejectValue("fullName", "", "Человек с таким ФИО уже существует");
    }
}