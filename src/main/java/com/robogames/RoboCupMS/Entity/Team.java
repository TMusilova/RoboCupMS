package com.robogames.RoboCupMS.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Tym pro soutezici
 */
@Entity(name = "team")
public class Team {

    /**
     * ID tymu
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Jmeno tymu
     */
    @Column(name = "name", length = 40, nullable = false, unique = true)
    private String name;

    /**
     * Vedouci tymu
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "leader_id", referencedColumnName = "id")
    private UserRC leader;

    /**
     * Clenove tymu
     */
    @OneToMany(mappedBy = "team")
    private List<UserRC> members;

    public Team(String _name, UserRC _leader) {
        this.name = _name;
        this.leader = _leader;
        this.members = new ArrayList<UserRC>();
        this.members.add(_leader);
    }

    /**
     * Navrati ID tymu
     * 
     * @return ID tymu
     */
    public Long getID() {
        return this.id;
    }

    /**
     * Navrati jmeno tymu
     * 
     * @return Jmeno tymu
     */
    public String getName() {
        return this.name;
    }

    /**
     * Navrati vsechny cleny tymu, vcetne vedouciho
     * 
     * @return Clenove tymu
     */
    public List<UserRC> getMembers() {
        return this.members;
    }

    /**
     * Nastavy nove jmeno tymu
     * 
     * @param _name Nove jmeno tymu
     */
    public void setName(String _name) {
        this.name = _name;
    }

    /**
     * Tymu priradi list s novymi cleny
     * 
     * @param _members List se cleny
     */
    public void setMembers(List<UserRC> _members) {
        this.members = _members;
    }

}
