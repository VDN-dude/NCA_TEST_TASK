package com.nca.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
public class Comments {

    @Id
    @GeneratedValue(generator = "Long")
    @GenericGenerator(
            name = "Long",
            strategy = "com.nca.generator.LongIdGenerator"
    )
    private Long id;

    @Column(length = 300)
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inserted_by_id", updatable = false)
    @CreatedBy
    private User insertedBy;

    @CreatedDate
    @Column(name = "creation_date", updatable = false)
    @Type(type= "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "news_id", updatable = false)
    private News news;

}
