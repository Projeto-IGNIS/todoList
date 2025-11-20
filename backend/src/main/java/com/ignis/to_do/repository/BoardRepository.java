package com.ignis.to_do.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ignis.to_do.model.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByTitleAndOwnerId(String title, Long ownerId);
    @Modifying
    @Query("UPDATE board b SET b.title = :title WHERE b.id = :id")
    void updateTitle(
        @Param("id") Long id,
        @Param("title") String title
        );

    @Modifying
    @Query("UPDATE board b SET b.favorite = :favorite WHERE b.id = :id")
    void updateFavorite(
        @Param("id") Long id,
        @Param("favorite") boolean favorite
        );

    

    
}