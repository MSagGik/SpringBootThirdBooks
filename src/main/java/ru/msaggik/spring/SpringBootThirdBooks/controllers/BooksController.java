package ru.msaggik.spring.SpringBootThirdBooks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.msaggik.spring.SpringBootThirdBooks.models.Book;
import ru.msaggik.spring.SpringBootThirdBooks.models.Person;
import ru.msaggik.spring.SpringBootThirdBooks.services.BooksService;
import ru.msaggik.spring.SpringBootThirdBooks.services.PeopleService;

import javax.validation.Valid;


@Controller
@RequestMapping("/books")
public class BooksController {

//    private final BookDAO bookDAO;
//    private final PersonDAO personDAO;
//    @Autowired
//    public BooksController(BookDAO BookDAO, PersonDAO personDAO) {
//        this.bookDAO = BookDAO;
//        this.personDAO = personDAO;
//    }

    private final BooksService booksService;
    private final PeopleService peopleService;

    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

//    @GetMapping()
//    public String index(Model model) {
//        model.addAttribute("books", bookDAO.index());
//        return "books/index";
//    }
    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_tear", required = false) boolean sortByYear) {
        // параметры с  required = false не обязательные и будут задействованы только при использовании
        if (page == null || booksPerPage == null) // если хотя-бы один из параметров не передан, то передаются все книги
            model.addAttribute("books", booksService.findAll(sortByYear)); // выдача всех книг без пагинации
        else // иначе выдача всех книг с пагинацией
            model.addAttribute("books", booksService.findWithPagination(page, booksPerPage, sortByYear));
        return "books/index";
    }

//    @GetMapping("/{id}")
//    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
//        model.addAttribute("book", bookDAO.show(id));
//        // просмотр пользователя (взявшего книгу) или назначение пользователя (берущего книгу)
//        Optional<Person> bookOwner = bookDAO.getBookOwner(id);
//
//        if (bookOwner.isPresent()) // если у книги есть пользователь
//            model.addAttribute("owner", bookOwner.get());
//        else
//            model.addAttribute("people", personDAO.index());
//
//        return "books/show";
//    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));
        // просмотр пользователя (взявшего книгу) или назначение пользователя (берущего книгу)
        Person bookOwner = booksService.getBookOwner(id);

        if (bookOwner!=null) // если у книги есть пользователь
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

//    @GetMapping("/new")
//    public String newBook(@ModelAttribute("book") Book Book) {
//        return "books/new";
//    }
//
//    @PostMapping()
//    public String create(@ModelAttribute("book") @Valid Book Book,
//                         BindingResult bindingResult) {
//        if (bindingResult.hasErrors())
//            return "books/new";
//
//        bookDAO.save(Book);
//        return "redirect:/books";
//    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book Book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        booksService.save(Book);
        return "redirect:/books";
    }

//    @GetMapping("/{id}/edit")
//    public String edit(Model model, @PathVariable("id") int id) {
//        model.addAttribute("book", bookDAO.show(id));
//        return "books/edit";
//    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

//    @PatchMapping("/{id}")
//    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
//                         @PathVariable("id") int id) {
//        if (bindingResult.hasErrors())
//            return "books/edit";
//
//        bookDAO.update(id, book);
//        return "redirect:/books";
//    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books";
    }

//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") int id) {
//        bookDAO.delete(id);
//        return "redirect:/books";
//    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

//    @PatchMapping("/{id}/release") // сдача книги в библиотеку
//    public String release(@PathVariable("id") int id) {
//        bookDAO.release(id);
//        return "redirect:/books/" + id;
//    }

    @PatchMapping("/{id}/release") // сдача книги в библиотеку
    public String release(@PathVariable("id") int id) {
        booksService.release(id);
        return "redirect:/books/" + id;
    }

//    @PatchMapping("/{id}/assign") // назначение книги пользователю
//    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
//        // у selectedPerson назначено только поле id, остальные поля - null
//        bookDAO.assign(id, selectedPerson);
//        return "redirect:/books/" + id;
//    }

    @PatchMapping("/{id}/assign") // назначение книги пользователю
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        // у selectedPerson назначено только поле id, остальные поля - null
        booksService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage() { // принимает get запросы и возвращает страницу поиска
        return "books/search";
    }

    @PostMapping("/search") // поиск значения вводимого значения "query"
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", booksService.searchByTitle(query));
        return "books/search";
    }
}
