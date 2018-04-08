package com.example.husain.assignment4;

import java.io.Serializable;

/**
 * Created by husain on 3/31/2018.
 */

public class SubjectData implements Serializable {

    private String title;
    private String id;
    private String college;
    private String classes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }


}
