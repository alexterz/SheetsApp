package com.SheetsApp.model;


import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ElementCollection;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import java.sql.Time;
import java.util.*;
import io.vavr.Tuple2;
import java.util.List;


@Entity
@Table(name = "coWorkers")
@Getter
@Setter
public class CoWorker extends Person{

    @NotBlank(message = "Speciality is mandatory")
    private String speciality;

    @ElementCollection
    private List< Tuple2<Integer,Integer>> availability;


    public CoWorker() {
    }

    public CoWorker(String lastName, String speciality) {
    	this.lastName = lastName;
    	this.speciality = speciality;
    }
    

}
