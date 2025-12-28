INSERT INTO groups (name, description, quantity)
VALUES
('ADMIN', 'Administrators with full access', 0),
('USER', 'Regular users with limited access', 0);

INSERT INTO permissions (name, description, group_id)
VALUES
('UPDATE_PROFILE', 'Permission to update own profile', (SELECT id FROM groups WHERE name = 'USER')),
('CREATE_ANNOUNCEMENT', 'Permission to create new announcements', (SELECT id FROM groups WHERE name = 'USER')),
('READ_ANNOUNCEMENT', 'Permission to view or search announcements', (SELECT id FROM groups WHERE name = 'USER')),
('UPDATE_ANNOUNCEMENT', 'Permission to update own announcements', (SELECT id FROM groups WHERE name = 'USER')),
('DELETE_ANNOUNCEMENT', 'Permission to delete own announcements', (SELECT id FROM groups WHERE name = 'USER')),

('CREATE_USER', 'Permission to create new users', (SELECT id FROM groups WHERE name = 'ADMIN')),
('READ_USER', 'Permission to view or search users', (SELECT id FROM groups WHERE name = 'ADMIN')),
('UPDATE_USER', 'Permission to update user information', (SELECT id FROM groups WHERE name = 'ADMIN')),
('DELETE_USER', 'Permission to delete user accounts', (SELECT id FROM groups WHERE name = 'ADMIN')),
('ASSIGN_GROUP', 'Permission to assign groups to users', (SELECT id FROM groups WHERE name = 'ADMIN')),
('REMOVE_GROUP', 'Permission to remove groups from users', (SELECT id FROM groups WHERE name = 'ADMIN')),
('CREATE_GROUP', 'Permission to create new groups', (SELECT id FROM groups WHERE name = 'ADMIN')),
('UPDATE_GROUP', 'Permission to update existing groups', (SELECT id FROM groups WHERE name = 'ADMIN')),
('DELETE_GROUP', 'Permission to delete groups', (SELECT id FROM groups WHERE name = 'ADMIN')),
('CREATE_PERMISSION', 'Permission to create new permissions', (SELECT id FROM groups WHERE name = 'ADMIN')),
('UPDATE_PERMISSION', 'Permission to update existing permissions', (SELECT id FROM groups WHERE name = 'ADMIN')),
('DELETE_PERMISSION', 'Permission to delete permissions', (SELECT id FROM groups WHERE name = 'ADMIN')),
('VIEW_AUDIT_LOGS', 'Permission to view audit logs', (SELECT id FROM groups WHERE name = 'ADMIN'));
