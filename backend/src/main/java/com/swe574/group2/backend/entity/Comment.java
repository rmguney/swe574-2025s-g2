package com.swe574.group2.backend.entity;

import com.swe574.group2.backend.enums.CommentType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "comments")
@Data
@EqualsAndHashCode(exclude = {"post", "parentComment", "replies", "mediaFiles", "upvotedBy", "downvotedBy"})
@ToString(exclude = {"post", "parentComment", "replies", "mediaFiles", "upvotedBy", "downvotedBy"})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "comment_type", columnDefinition = "VARCHAR(50) DEFAULT 'QUESTION'")
    private CommentType commentType = CommentType.QUESTION;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaFile> mediaFiles;

    @ManyToMany
    @JoinTable(
            name = "comment_upvotes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> upvotedBy;

    @ManyToMany
    @JoinTable(
            name = "comment_downvotes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> downvotedBy;

    @Column(nullable = false)
    private int upvotesCount = 0;

    @Column(nullable = false)
    private int downvotesCount = 0;

    @Column(nullable = false)
    private boolean isBestAnswer = false;
}
