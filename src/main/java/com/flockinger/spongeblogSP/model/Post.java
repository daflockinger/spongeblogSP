package com.flockinger.spongeblogSP.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import com.flockinger.spongeblogSP.model.enums.PostStatus;

@Entity
@Audited
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})},
    indexes = {@Index(columnList = "title"),
        // for editing
        @Index(columnList = "author_id"), @Index(columnList = "author_id,category_id"),
        @Index(columnList = "author_id,status"),
        // for view
        @Index(columnList = "status"), @Index(columnList = "status,category_id")})
public class Post extends BaseModel {

  @NotNull
  @Length(max = 150)
  @Column(columnDefinition = "VARCHAR(150)")
  private String title;

  @NotNull
  @Column(columnDefinition = "TEXT")
  private String content;

  @NotNull
  private Date created;

  private Date modified;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "VARCHAR(150)")
  private PostStatus status;

  @ManyToOne(fetch = FetchType.EAGER)
  private User author;

  @ManyToOne(optional=true, fetch = FetchType.EAGER)
  private Category category;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "post_tags",
      joinColumns = @JoinColumn(name = "posts_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
  private List<Tag> tags;


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getModified() {
    return modified;
  }

  public void setModified(Date modified) {
    this.modified = modified;
  }

  public PostStatus getStatus() {
    return status;
  }

  public void setStatus(PostStatus status) {
    this.status = status;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
}
