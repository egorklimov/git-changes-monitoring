package com.github.egorklimov.data.repository.contributor;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "contributor")
@NoArgsConstructor
public class Contributor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mail;
    private String name;

    public Contributor(String mail,
                       String name) {
        this.mail = mail;
        this.name = name;
    }
}
