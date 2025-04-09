package com.ignis.to_do.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ignis.to_do.model.TaskList;


@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long>{

    Optional<TaskList> findByName(String name);

    Optional<TaskList> findById(Long id);

    @Query("SELECT tl FROM task_list tl WHERE tl.board.id = :boardId")
    Iterable<TaskList> findByBoardId(@Param("boardId") Long boardId);   
    

    @Modifying
    @Query("UPDATE task_list tl SET tl.board.id = :boardId WHERE tl.id = :id")
    void updateBoardId(
        @Param("id") Long id,
        @Param("boardId") Long boardId
        );

    @Modifying
    @Query("UPDATE task_list tl SET tl.name = :name WHERE tl.id = :id")
    void updateTaskListTitle(
        @Param("id") Long id,
        @Param("name") String name
        );
}