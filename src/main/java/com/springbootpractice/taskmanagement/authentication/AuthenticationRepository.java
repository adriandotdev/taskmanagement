package com.springbootpractice.taskmanagement.authentication;

import com.springbootpractice.taskmanagement.config.jwtauth.JwtUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AuthenticationRepository {

    @Autowired
    private JdbcTemplate template;

    public int register(JwtUserDetail jwtUserDetail) {

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        final String query = "INSERT INTO users (username, password, role) VALUES (?,?,?)";

        int rowsAffected = this.template.update(conn -> {

            PreparedStatement preparedStatement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, jwtUserDetail.getUsername());
            preparedStatement.setString(2, jwtUserDetail.getPassword());
            preparedStatement.setString(3, jwtUserDetail.getRole());

            return preparedStatement;
        }, generatedKeyHolder);

        return Objects.requireNonNull(generatedKeyHolder.getKey()).intValue();
    }

    public void insertUserDetails(int userID, RegisterRequest request) {

        final String query = "INSERT INTO user_details (user_id, given_name, middle_name, last_name) VALUES (?,?,?,?)";

        this.template.update(conn -> {

            PreparedStatement preparedStatement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, request.givenName());
            preparedStatement.setString(3, request.middleName());
            preparedStatement.setString(4, request.lastName());

            return preparedStatement;

        }, new GeneratedKeyHolder());
    }

    public Optional<JwtUserDetail> getUserByUsername(String username) {

        String query = "SELECT username, password, role FROM users WHERE username = ?";

        RowMapper<JwtUserDetail> map = (ResultSet rs, int index) ->

            new JwtUserDetail.UserDetailBuilder()
                    .setUsername(rs.getString("username"))
                    .setPassword(rs.getString("password"))
                    .setRole(JwtUserDetail.ROLES.valueOf(rs.getString("role")))
                    .build();
        ;

        try {
            JwtUserDetail jwtUserDetail = this.template.queryForObject(query, map, username);

            if (jwtUserDetail != null)
                return Optional.of(jwtUserDetail);
            else
                Optional.empty();
        }
        catch(DataAccessException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }
}
