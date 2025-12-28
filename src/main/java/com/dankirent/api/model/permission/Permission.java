package com.dankirent.api.model.permission;

import com.dankirent.api.model.group.Group;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Permission {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne()
    @JoinColumn(name = "group_id")
    private Group group;

}
