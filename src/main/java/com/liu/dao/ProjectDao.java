package com.liu.dao;

import com.liu.domain.Project;
import com.liu.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectDao {
    public void save(Project project) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "insert into class(className,packageName,url) values(?,?,?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, project.getClassName());
            ps.setString(2, project.getPackageName());
            ps.setString(3, project.getUrl());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, ps);
        }

    }
}
