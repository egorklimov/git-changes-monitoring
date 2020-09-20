package com.github.egorklimov.data.repository.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "repository_has_tag")
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryHasTag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "tag_id")
  private Long tagId;
  @Column(name = "repository_id")
  private Long repositoryId;

  public RepositoryHasTag(Long tagId, Long repositoryId) {
    this.tagId = tagId;
    this.repositoryId = repositoryId;
  }
}