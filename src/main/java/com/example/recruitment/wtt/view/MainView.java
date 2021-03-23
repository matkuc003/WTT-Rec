package com.example.recruitment.wtt.view;
import com.example.recruitment.wtt.model.Book;
import com.example.recruitment.wtt.service.BookService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route
public class MainView extends VerticalLayout {

    BookService bookService;

    public MainView(BookService bookService) {
        this.bookService = bookService;
        init();
    }

    private void init() {
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.setLabel("Category: ");
        Grid<Book> grid = new Grid<>();
        grid.addColumn(Book::getTitle).setHeader("Title");
        grid.addColumn(Book::getAuthors).setHeader("Authors");
        grid.addColumn(Book::getAverageRating).setHeader("Rating");
        grid.addColumn(Book::getPublishedDate).setHeader("Published Date");
        categoryComboBox.setItems(bookService.getCategories());
        categoryComboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                String valueFromCB = event.getValue();
                List<Book> booksList = bookService.getBooksByCategory(valueFromCB).getBody();
                grid.setItems(booksList);
            }
        });
        add(categoryComboBox, grid);
    }
}
