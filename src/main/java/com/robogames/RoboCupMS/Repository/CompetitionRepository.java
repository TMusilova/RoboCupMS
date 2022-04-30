package com.robogames.RoboCupMS.Repository;

import com.robogames.RoboCupMS.Entity.Competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repozitar pro tymy
 */
@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

}
