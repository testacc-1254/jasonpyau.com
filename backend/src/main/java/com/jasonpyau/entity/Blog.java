package com.jasonpyau.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "blogs", indexes = @Index(name = "unix_time_ind", columnList = "unix_time"))
public class Blog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column (name = "title")
    private String title;
    @Column (name = "body", columnDefinition = "varchar(5000)")
    private String body;
    @Column (name = "like_count")
    private Integer likeCount;
    @Column (name = "view_count")
    private Long viewCount;
    @Column (name = "date")
    private String date;
    @Column (name = "unix_time")
    private Long unixTime;
    @Getter(AccessLevel.NONE)
    @Column (name = "liked_users")
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "blog_user", 
                joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> likedUsers = new HashSet<>();
    @Transient
    private Boolean isLikedByUser;

    public void view() {
        this.viewCount++;
    }

    public void like(User user) {
        user.getLikedBlogs().add(this);
        this.likedUsers.add(user);
        this.likeCount = likedUsers.size();
    }

    public void unlike(User user) {
        user.getLikedBlogs().remove(this);
        this.likedUsers.remove(user);
        this.likeCount = likedUsers.size();
    }

    public void checkIsLikedByUser(User user) {
        this.isLikedByUser = likedUsers.contains(user);
    }
}
