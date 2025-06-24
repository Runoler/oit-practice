package org.example.services;

import org.example.dao.ActivityDAO;
import org.example.dao.FormDAO;
import org.example.dao.FormTagDAO;
import org.example.dao.TagDAO;
import org.example.entities.Form;
import org.example.entities.Tag;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FormService extends BaseService {
    private FormDAO formDAO;
    private TagDAO tagDAO;
    private ActivityService activityService;
    private TagService tagService;
    private FormTagDAO formTagDAO;

    public FormService(Connection connection) {
        super(connection);
        this.formDAO = new FormDAO(getConnection());
        this.activityService = new ActivityService(getConnection());
        this.tagService = new TagService(getConnection());
        this.tagDAO = new TagDAO(getConnection());
        this.formTagDAO = new FormTagDAO(getConnection(), formDAO, tagDAO);
    }

    public Optional<Form> createForm(UUID activityId, String title, String formText) {
        if (!activityService.getActivityById(activityId).isPresent()) {
            System.out.println("Активность с ID " + activityId + " не найдена.");
            return Optional.empty();
        }
        Form newForm = new Form(activityId, title, formText);
        return formDAO.create(newForm);
    }

    public Optional<Form> getFormById(UUID formId) {
        return formDAO.findById(formId);
    }

    public List<Form> getAllForms() {
        return formDAO.findAll();
    }

    public Optional<Form> updateForm(Form form) {
        if (form.getActivityId() != null && !activityService.getActivityById(form.getActivityId()).isPresent()) {
            System.out.println("Активность с ID " + form.getActivityId() + " не найдена для обновления формы.");
            return Optional.empty();
        }
        return formDAO.update(form);
    }

    public boolean deleteForm(UUID formId) {
        return formDAO.delete(formId);
    }

    public boolean addTagToForm(UUID formId, UUID tagId) {
        Optional<Form> form = getFormById(formId);
        Optional<Tag> tag = tagService.getTagById(tagId);

        if (form.isPresent() && tag.isPresent()) {
            boolean success = formTagDAO.addTagToForm(formId, tagId);
            return success;
        }
        System.out.println("Форма или тег не найдены для присвоения.");
        return false;
    }

    public boolean removeTagFromForm(UUID formId, UUID tagId) {
        Optional<Form> form = getFormById(formId);
        Optional<Tag> tag = tagService.getTagById(tagId);

        if (form.isPresent() && tag.isPresent()) {
            boolean success = formTagDAO.removeTagFromForm(formId, tagId);
            return success;
        }
        System.out.println("Форма или тег не найдены для удаления.");
        return false;
    }

    public List<Tag> getFormTags(UUID formId) {
        Optional<Form> form = getFormById(formId);
        if (form.isPresent()) {
            return formTagDAO.findTagsByFormId(formId);
        }
        System.out.println("Форма не найдена для получения тегов.");
        return new ArrayList<>();
    }
}
