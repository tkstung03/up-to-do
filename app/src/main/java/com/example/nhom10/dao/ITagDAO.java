package com.example.nhom10.dao;

import com.example.nhom10.model.Tag;

import java.util.List;

public interface ITagDAO {
    Tag getById(int id);

    List<Tag> getAll();

    boolean update(Tag tag);

    boolean delete(int id);
}
