package com.robogames.RoboCupMS.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.robogames.RoboCupMS.Enum.EMatchState;

/**
 * Entita reprezentujici mozne stavy zapasu
 */
@Entity(name = "match_state")
public class MatchState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EMatchState name;

    public MatchState() {
    }

    public MatchState(EMatchState name) {
        this.name = name;
    }

    public Integer getID() {
        return id;
    }

    public void getID(Integer id) {
        this.id = id;
    }

    public EMatchState getName() {
        return name;
    }

    public void setName(EMatchState name) {
        this.name = name;
    }

}
