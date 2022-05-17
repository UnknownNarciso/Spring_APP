package com.example.application.views.list;

import java.util.Collections;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.HorizontalAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import antlr.debug.Event;

@PageTitle("")
@Route(value = "",layout = MainLayout.class)
public class ListView extends VerticalLayout {
Grid<Contact> grid =  new  Grid<>(Contact.class);
TextField filtertText = new TextField();
ContactForm form;
private CrmService service;

    public ListView(CrmService service) {
        this.service = service ;
      addClassName("list-view");
      setSizeFull();
      configureGrid();
      configureForm();
      add(
          getToolbar(),
          getContent()
      );
      updateList();
      closeEditor();
    }
    private void closeEditor()
    {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }
    private void updateList()
    {
        grid.setItems(service.findAllContacts(filtertText.getValue()));
    }
    private Component getContent()
    {
        HorizontalLayout content = new HorizontalLayout(grid,form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();
        return content;
        
    }
    private Component getToolbar()
    {
    filtertText.setPlaceholder("Filter by name ...  ");
    filtertText.setClearButtonVisible(true);
    filtertText.setValueChangeMode(ValueChangeMode.LAZY);
    filtertText.addValueChangeListener(e -> updateList());
    Button addContactButton = new Button("Add contact");
    addContactButton.addClickListener(e -> addContact());
    HorizontalLayout toolbar  = new HorizontalLayout(filtertText, addContactButton);
    toolbar.addClassName("toolbar");
    return toolbar;
}
private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }
private void configureForm()
{
    form = new ContactForm(service.findAllCompanies(),service.FindAllStatuses());
    form.setWidth("25em");

    form.addListener(ContactForm.SaveEvent.class, this::SaveContact);
    form.addListener(ContactForm.DeleteEvent.class,this::deleteContact);
    form.addListener( ContactForm.CloseEvent.class, e -> closeEditor());
}
private void SaveContact(ContactForm.SaveEvent event)
{
    service.SaveContact(event.getContact());
    updateList();
    closeEditor();
}
private void deleteContact(ContactForm.DeleteEvent event)
{
    service.deleteContact(event.getContact());
    updateList();
    closeEditor();
}
private void configureGrid()
{
    grid.addClassName("contact-grid");
    grid.setSizeFull();
    grid.setColumns("firstName","lastName","email");
    grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
    grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
    grid.getColumns().forEach(col -> col.setAutoWidth(true));

    grid.asSingleSelect().addValueChangeListener(e -> editContact(e.getValue()));

}
private void editContact(Contact contact) {
    if(contact == null)
    {
  closeEditor();
    }else{
        form.setContact(contact);
        form.setVisible(true);
        addClassName("editing");
    }

}
}
