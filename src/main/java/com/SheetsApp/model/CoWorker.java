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
//import io.vavr.Tuple2;
//import io.vavr.jackson;
//import com.fasterxml.jackson.annotation.*;
import java.util.List;


@Entity
@Table(name = "coWorkers")
@Getter
@Setter
public class CoWorker extends Person{

    @NotBlank(message = "Sector field is mandatory")
    private String sector;

    @ElementCollection
    private List<Integer> availability;


    public CoWorker() {
    }

    public CoWorker(String lastName, String sector) {
    	this.lastName = lastName;
    	this.sector = sector;
    }
    

}
