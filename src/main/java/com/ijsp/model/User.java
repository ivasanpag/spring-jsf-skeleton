package com.ijsp.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author ijsp
 * @since
 */
@Entity
public class User {

    @Id
    private Long id;
    private String username;
    private String password;

}
