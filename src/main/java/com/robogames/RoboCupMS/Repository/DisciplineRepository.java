package com.robogames.RoboCupMS.Repository;

import com.robogames.RoboCupMS.Entity.Discipline;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repozitar pro stavy, ve kterych se muze zapas nachazet
 */
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {

}
