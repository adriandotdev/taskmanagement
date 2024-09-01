package com.springbootpractice.taskmanagement.authentication;

import com.springbootpractice.taskmanagement.config.basicauth.BasicUserDetail;
import com.springbootpractice.taskmanagement.config.jwtauth.UserDetail;
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

    public void register(UserDetail userDetail) {

        final String query = "INSERT INTO users (username, password, role) VALUES (?,?,?)";

        this.template.update(query, userDetail.getUsername(), userDetail.getPassword(), userDetail.getRole());
    }

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

    public Optional<BasicUserDetail> getBasicTokenByUsername(String username) {
        String query = "SELECT username, password FROM tokens WHERE username = ?";

        RowMapper<BasicUserDetail> mapper = (ResultSet rs, int index) ->
            new BasicUserDetail
                    .BasicUserDetailBuilder()
                    .setUsername(rs.getString("username"))
                    .setPassword(rs.getString("password"))
                    .build();

        try {
            BasicUserDetail basicUserDetail = this.template.queryForObject(query, mapper, username);

            if (basicUserDetail != null) {
                return Optional.of(basicUserDetail);
            }

            return Optional.empty();
        }
        catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
