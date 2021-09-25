package com.ap.greenpole.transactioncomponent.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 1:31 PM
 */

@Entity(name = "director")
public class Director  implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_company_id", referencedColumnName = "client_company_id")
    @JsonManagedReference
    private ClientCompany clientCompany;
}
