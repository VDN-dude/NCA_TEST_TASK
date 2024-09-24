package com.nca.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "news")
@EntityListeners(AuditingEntityListener.class)
public class News {

    @Id
    @GeneratedValue(generator = "Long")
    @GenericGenerator(
            name = "Long",
            strategy = "com.nca.generator.LongIdGenerator"
    )
    private Long id;

    @Column(length = 150)
    private String title;
    @Column(length = 2000)
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inserted_by_id")
    private User insertedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    @CreatedDate
    @Column(name = "creation_date",  insertable = true, updatable = false)
    @Type(type= "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "last_edit_date", insertable = false, updatable = true)
    @Type(type= "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime lastEditDate;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "news")
    private List<Comments> comments = new ArrayList<>();
}
