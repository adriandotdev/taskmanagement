package com.springbootpractice.taskmanagement.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate template;

    public List<Users> GetUsers() {

        final String QUERY = """
                SELECT 
                    id,
                    username,
                    role,
                    date_created,
                    date_modified
                FROM 
                    users;
                """;
        RowMapper<Users> mapper = (ResultSet rs, int index) ->
                new Users(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getDate(5)
                );

        return this.template.query(QUERY, mapper);
    }
}
