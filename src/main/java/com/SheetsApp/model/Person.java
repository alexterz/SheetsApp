package com.SheetsApp.model;

import java.util.Objects;
import javax.persistence.MappedSuperclass;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;


@MappedSuperclass
@Getter
@Setter
public abstract class Person extends Location{

    @Id
   // @NotBlank(message = "Id is mandatory")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @NotBlank(message = "LastName is mandatory")
    protected String lastName;

    protected String afm,firstName,email,phone;
    

}
