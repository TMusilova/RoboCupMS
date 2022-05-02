package com.robogames.RoboCupMS.Repository;

import com.robogames.RoboCupMS.Entity.Match;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repozitar pro zapasove skupiny
 */
public interface MatchRepository extends JpaRepository<Match, Long> {

}
