package com.springbootpractice.taskmanagement.users;

import java.util.Date;

public record Users(

        int id,
        String username,
        String role,
        Date date_created,
        Date date_modified
) {
}
