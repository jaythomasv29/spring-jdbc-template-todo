package com.sg.jdbctemplateexample;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ToDoMapper implements RowMapper<ToDo> {
    @Override
    public ToDo mapRow(ResultSet rs, int index) throws SQLException {
        ToDo td = new ToDo();
        td.setId(rs.getInt("id"));
        td.setTodo(rs.getString("todo"));
        td.setNote(rs.getString("note"));
        td.setFinished(rs.getBoolean("finished"));
        return td;
    }
}
