package jpql.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private int age;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    protected Member() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public Team getTeam() {
        return team;
    }
    public List<Order> getOrders() {
        return orders;
    }

    //==연관관계 편의 메서드==//

    public static Member createMember(String username, int age) {
        Member member = new Member();
        member.username = username;
        member.age = age;
        return member;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
