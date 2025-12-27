package com.dankirent.api.model.user;

import com.dankirent.api.model.user.pk.UserGroupPk;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_groups")
public class UserGroup {

    @EmbeddedId
    private final UserGroupPk  id = new UserGroupPk();

    public UserGroup(Group group, User user) {
        this.id.setGroup(group);
        this.id.setUser(user);
    }

    public User getUser() {
        return this.id.getUser();
    }

    public void setUser(User user) {
        this.id.setUser(user);
    }

    public Group getGroup() {
        return this.id.getGroup();
    }

    public void setGroup(Group group) {
        this.id.setGroup(group);
    }
}
