package com.springbootpractice.taskmanagement.authentication;

import com.springbootpractice.taskmanagement.config.UserDetail;
import com.springbootpractice.taskmanagement.config.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Optional;

@Repository
public class AuthenticationRepository {

    @Autowired
    private JdbcTemplate template;

    public Optional<UserDetail> getUserByUsername(String username) {

        String query = "SELECT username, password, role FROM users WHERE username = ?";

        RowMapper<UserDetail> map = (ResultSet rs, int index) ->

            new UserDetail.UserDetailBuilder()
                    .setUsername(rs.getString("username"))
                    .setPassword(rs.getString("password"))
                    .setRole(UserDetail.ROLES.valueOf(rs.getString("role")))
                    .build();
        ;

        try {
            UserDetail userDetail = this.template.queryForObject(query, map, username);

            if (userDetail != null)
                return Optional.of(userDetail);
        }
        catch(DataAccessException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }
}
