package com.example.guide.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attraction_id", nullable = false)
    private Attraction attraction;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false)
    private int score;

    protected Rating() {
    }

    public Rating(Long id, Attraction attraction, String author, int score) {
        this.id = id;
        this.attraction = attraction;
        this.author = author;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public String getAuthor() {
        return author;
    }

    public int getScore() {
        return score;
    }
}
