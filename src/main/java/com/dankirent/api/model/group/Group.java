package com.dankirent.api.model.group;

import com.dankirent.api.model.group.dto.GroupRequestDto;
import com.dankirent.api.model.group.dto.GroupUpdateDto;
import com.dankirent.api.model.permission.Permission;
import com.dankirent.api.model.user.UserGroup;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "id.group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserGroup> userGroups = new HashSet<UserGroup>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Permission> permissions = new HashSet<Permission>();

    public Group(GroupRequestDto dto) {
        this.name = dto.name();
        this.description = dto.description();
    }

    public Group(GroupUpdateDto dto) {
        this.name = dto.name();
        this.description = dto.description();
    }
}
