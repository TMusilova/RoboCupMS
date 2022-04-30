package com.robogames.RoboCupMS.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @OneToOne
    @JoinColumn(name = "leader_id", referencedColumnName = "id")
    private UserRC leader;

    /**
     * Clenove tymu
     */
    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
    private List<UserRC> members;

    public Team() {
    }

    public Team(String _name, UserRC _leader) {
        this.name = _name;
        this.leader = _leader;
        this.members = new ArrayList<UserRC>();
        this.addMember(_leader);
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
     * Navrati ID vedouciho tymu
     * 
     * @return Vedouci tymu
     */
    public long getLeaderID() {
        if (this.leader == null) {
            return -1;
        } else {
            return this.leader.getID();
        }
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
     * Do tymu prida noveho clena
     * 
     * @param _member Novy clen
     */
    public void addMember(UserRC _member) {
        if (_member != null) {
            this.members.add(_member);
            _member.setTeam(this);
        }
    }

    /**
     * Z tymu odebere clena
     * 
     * @param _member Novy clen
     */
    public void removeMember(UserRC _member) {
        if (_member != null) {
            this.members.remove(_member);
            _member.setTeam(null);
        }
    }

}
