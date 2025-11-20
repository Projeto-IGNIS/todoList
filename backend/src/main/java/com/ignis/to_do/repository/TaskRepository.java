package com.ignis.to_do.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ignis.to_do.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Modifying
    @Query("UPDATE task tk SET tk.title = :title WHERE tk.id = :id")
    void updateTaskTitle(@Param("id") Long id, @Param("title") String title);

    @Modifying
    @Query("UPDATE task tk SET tk.list.id = :listId WHERE tk.id = :id")
    void updateTaskList(@Param("id") Long id, @Param("listId") Long listId);
}   