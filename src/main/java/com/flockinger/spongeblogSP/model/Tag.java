package com.flockinger.spongeblogSP.model;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

@Entity
@Audited
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})},
    indexes = {@Index(columnList = "name")})
@NamedQuery(name = "Tag.findFromPreviousPost",
    query = "select t from PostTagsAud p join Tag t on p.tagId=t.id where p.postsId = :postId and p.rev = :revId")
public class Tag extends BaseModel {

  @NotNull
  @Length(max = 150)
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
