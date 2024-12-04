package com.example.nhom10.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tasks {
    private int taskId;
    private String title;
    private String description;
    private Date dueDate;
    private Date createAt;
    private Date updateAt;
    private int user_id;
    private int categoryId;


}
