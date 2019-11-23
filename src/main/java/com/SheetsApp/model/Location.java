package com.SheetsApp.model;

import java.util.Objects;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;


@MappedSuperclass
@Getter
@Setter
public abstract class Location {

   // @NotNull
    private String PostalCode;

    private String City;
    
   // @NotNull
    private String Address;

    private String Number;
}