package org.example.services;

import org.example.dao.TagDAO;
import org.example.entities.Tag;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TagService extends BaseService {
    private TagDAO tagDAO;

    public TagService(Connection connection) {
        super(connection);
        this.tagDAO = new TagDAO(getConnection());
    }

    public Optional<Tag> createTag(String tagName) {
        Tag newTag = new Tag(tagName);
        return tagDAO.create(newTag);
    }

    public Optional<Tag> getTagById(UUID tagId) {
        return tagDAO.findById(tagId);
    }

    public List<Tag> getAllTags() {
        return tagDAO.findAll();
    }

    public Optional<Tag> updateTag(Tag tag) {
        return tagDAO.update(tag);
    }

    public boolean deleteTag(UUID tagId) {
        return tagDAO.delete(tagId);
    }
}
