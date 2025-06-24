package org.example.app;

import org.example.DBConnectionProvider;
import org.example.entities.*;
import org.example.services.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ApplicationRunner {

    public static void main(String[] args) {
        
        Connection connection = null;
        try {
            connection = DBConnectionProvider.getConnection();
            System.out.println("Database connection established successfully.");

            RoleService roleService = new RoleService(connection);
            ActivityService activityService = new ActivityService(connection);
            TagService tagService = new TagService(connection);
            FormService formService = new FormService(connection);
            UserService userService = new UserService(connection);
            UserCommentService userCommentService = new UserCommentService(connection);
            UserFormAssociationService userFormAssociationService = new UserFormAssociationService(connection);


            System.out.println("\n--- User registration ---");
            Optional<User> user1Optional = userService.registerUser("user1", "user1@example.com", "pass123", "avatar1.png");
            User user1 = user1Optional.orElse(null);
            if (user1 != null) {
                System.out.println("User registered: " + user1.getUsername() + " (ID: " + user1.getId() + ")");
            }

            Optional<User> user2Optional = userService.registerUser("user2", "user2@example.com", "pass456", "avatar2.png");
            User user2 = user2Optional.orElse(null);
            if (user2 != null) {
                System.out.println("User registered: " + user2.getUsername() + " (ID: " + user2.getId() + ")");
            }

            System.out.println("\n--- Role creation ---");
            Optional<Role> adminRoleOptional = roleService.createRole("ADMIN");
            Role adminRole = adminRoleOptional.orElse(null);
            if (adminRole != null) {
                System.out.println("Role created: " + adminRole.getRoleName() + " (ID: " + adminRole.getId() + ")");
            }

            Optional<Role> memberRoleOptional = roleService.createRole("MEMBER");
            Role memberRole = memberRoleOptional.orElse(null);
            if (memberRole != null) {
                System.out.println("Role created: " + memberRole.getRoleName() + " (ID: " + memberRole.getId() + ")");
            }

            System.out.println("\n--- User's role assign ---");
            if (user1 != null && adminRole != null) {
                userService.assignRoleToUser(user1.getId(), adminRole.getId());
            }
            if (user2 != null && memberRole != null) {
                userService.assignRoleToUser(user2.getId(), memberRole.getId());
            }

            System.out.println("\n--- Get all user roles ---");
            if (user1 != null) {
                List<Role> user1Roles = userService.getUserRoles(user1.getId());
                System.out.println("User roles " + user1.getUsername() + ": " + user1Roles.stream().map(Role::getRoleName).collect(java.util.stream.Collectors.joining(", ")));
            }

            System.out.println("\n--- Activity creation ---");
            Optional<Activity> projectActivityOptional = activityService.createActivity("Games");
            Activity projectActivity = projectActivityOptional.orElse(null);
            if (projectActivity != null) {
                System.out.println("Activity created: " + projectActivity.getActivityName() + " (ID: " + projectActivity.getId() + ")");
            }

            System.out.println("\n--- Form creation ---");
            Optional<Form> form1Optional = Optional.empty();
            if (projectActivity != null) {
                form1Optional = formService.createForm(projectActivity.getId(), "Monopoly", "I'm looking for monopoly players.");
            }
            Form form1 = form1Optional.orElse(null);
            if (form1 != null) {
                System.out.println("Form created: " + form1.getTitle() + " (ID: " + form1.getId() + ")");
            }

            System.out.println("\n--- Tag creation ---");
            Optional<Tag> monopolyTagOptional = tagService.createTag("Monopoly");
            Tag monopolyTag = monopolyTagOptional.orElse(null);
            if (monopolyTag != null) { System.out.println("Tag created: " + monopolyTag.getTagName() + " (ID: " + monopolyTag.getId() + ")"); }

            Optional<Tag> minskTagOptional = tagService.createTag("Minsk");
            Tag minskTag = minskTagOptional.orElse(null);
            if (minskTag != null) { System.out.println("Tag created: " + minskTag.getTagName() + " (ID: " + minskTag.getId() + ")"); }

            System.out.println("\n--- Form tag assign ---");
            if (form1 != null && monopolyTag != null) {
                formService.addTagToForm(form1.getId(), monopolyTag.getId());
            }
            if (form1 != null && minskTag != null) {
                formService.addTagToForm(form1.getId(), minskTag.getId());
            }

            System.out.println("\n--- Get form tags ---");
            if (form1 != null) {
                List<Tag> form1Tags = formService.getFormTags(form1.getId());
                System.out.println("Form tags '" + form1.getTitle() + "': " + form1Tags.stream().map(Tag::getTagName).collect(java.util.stream.Collectors.joining(", ")));
            }

            System.out.println("\n--- Creating user-form association ---");
            if (user1 != null && form1 != null) {
                Optional<UserFormAssociation> ufaOptional = userFormAssociationService.createUserFormAssociation(user1.getId(), form1.getId(), true);
                ufaOptional.ifPresent(ufa -> System.out.println("Association created User-Form: User " + ufa.getUserId() + " is leader of form " + ufa.getFormId()));
            }

            System.out.println("\n--- User update ---");
            if (user1 != null) {
                user1.setPasswordHash("new_hashed_password");
                userService.updateUser(user1);
                System.out.println("User password " + user1.getUsername() + " updated.");
            }

            System.out.println("\n--- User login ---");
            if (user1 != null) {
                userService.loginUser(user1.getEmail(), "new_hashed_password");
            }

            System.out.println("\n--- Add a comment ---");
            if (user1 != null && user2 != null) {
                userCommentService.addComment(user1.getId(), user2.getId(), "Nice game master!", 1);
            }

            System.out.println("\n--- User deleting ---");
            if (user1 != null) {
                userService.deleteUser(user1.getId());
                Optional<User> deletedUser = userService.getUserById(user1.getId());
                deletedUser.ifPresent(u -> System.out.println("User " + u.getUsername() + " is_deleted: " + u.isDeleted()));
            }


        } catch (SQLException e) {
            System.err.println("Error database connection or query executing: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Database connection closed.");
                } catch (SQLException e) {
                    System.err.println("Error database connection closing: " + e.getMessage());
                }
            }
        }
    }
}