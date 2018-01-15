package com.example.bookgroup.controllers;

import com.example.bookgroup.models.Book;
import com.example.bookgroup.models.BookGenre;
import com.example.bookgroup.models.Members;
import com.example.bookgroup.models.data.BookDao;
import com.example.bookgroup.models.data.MembersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "book")
public class BookController {

    private Book book = Book.getInstance();

    @Autowired
    private BookDao bookDao;

    @Autowired
    private MembersDao membersDao;



    //welcome page


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("books", bookDao.findAll());
        model.addAttribute("title", "Book Group Reading List");
        return "book/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayBookAddForm(Model model) {
        model.addAttribute("title", "Add Book");
        model.addAttribute(new Book());
        model.addAttribute("bookGenres", BookGenre.values());
        model.addAttribute("memberss", membersDao.findAll());
        return "book/add";
    }

    //?MODEL BINDING
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processBookAddForm(@ModelAttribute @Valid Book newBook, Errors errors,
                                     @RequestParam int membersId, Model model) {
       if (newBook.getTitle().isEmpty()) {
            model.addAttribute("title", "Add Book");
            model.addAttribute("members", membersDao.findAll());
            return "book/add";
        }

        Members mem = membersDao.findOne(membersId);
        newBook.setMembers(mem);
        bookDao.save(newBook);

        return "redirect:/book";
    }



    //edit a book's details
    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String displayEditForm(@PathVariable int id, Model model) {
        Book editBook = bookDao.findOne(id);

        model.addAttribute("title", editBook.getTitle());
        model.addAttribute("authorFirstName", editBook.getAuthorFirstName());
        model.addAttribute("authorLastName", editBook.getAuthorLastName());
        model.addAttribute("bookId", bookDao.findOne(id));

        return "book/edit";
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
    public String processEditForm(int id, String title, String authorFirstName, String authorLastName) {
        Book updateBook = bookDao.findOne(id);
        updateBook.setTitle(title);
        updateBook.setAuthorFirstName(authorFirstName);
        updateBook.setAuthorLastName(authorLastName);

        bookDao.save(updateBook);

        return "redirect:/book";
    }

    //show one book's details
    @RequestMapping(value="detail/{id}", method = RequestMethod.GET)
    public String displayDetailsForm(@PathVariable int id, Model model) {
        Book displayBook = bookDao.findOne(id);
        model.addAttribute("title", displayBook.getTitle());
        model.addAttribute("bookId", bookDao.findOne(id));
        model.addAttribute(displayBook);

        return "book/detail";

    }

        //TODO list of recommended books
    @RequestMapping(value="recommended", method = RequestMethod.GET)
    public String displayRecommended(Model model) {

        ArrayList<Book> books;

        model.addAttribute("books", book.findRecs(1));


        return "book/recommended";
    }



    //rate a book
    @RequestMapping(value = "rate/{id}", method = RequestMethod.GET)
    public String displayRateForm(@PathVariable int id, Model model) {
        Book rateBook = bookDao.findOne(id);

        model.addAttribute("booktitle", rateBook.getTitle());
        //model.addAttribute("bookAuthorFirst",rateBook.getAuthorFirstName());
        model.addAttribute("author", rateBook.getAuthorLastName());
        model.addAttribute("bookId", rateBook.getId());

        return "book/rate";
    }

    @RequestMapping(value = "rate/{id}", method = RequestMethod.POST)
    public String processRateForm(int id, double rate, Model model) {
        Book rateBook = bookDao.findOne(id);
        rateBook.setRate(rate);

        //List rtngs = rateBook.getRatings();
        //rtngs.add(rate);
        //rateBook.calculateAverage(rateBook.getRatings());
        //rateBook.setRatings(rtngs);



        rateBook.setAverageRating(rateBook.calculateAverage(rateBook.getRatings()));
        //rateBook.getAverageRating();
       // rateBook.setAverageRating(rateBook.calculateAverage());

       // model.addAttribute("avg", rateBook.calculateAverage());
        bookDao.save(rateBook);

        return "redirect:/book";
    }
}


