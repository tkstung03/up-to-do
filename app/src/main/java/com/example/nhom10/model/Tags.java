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
public class Tags {
    private int tagId;
    private String name;
    private Date createAt;
    private String color;
}
