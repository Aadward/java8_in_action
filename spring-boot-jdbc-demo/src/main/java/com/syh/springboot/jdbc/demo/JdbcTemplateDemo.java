package com.syh.springboot.jdbc.demo;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.syh.springboot.jdbc.demo.model.DbTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is just a DEMO for usage of JdbcTemplate in Spring framework. It contains
 * the base operations of creating, deleting, retrieving and updating.
 *
 * @author Yuhang Shen
 */

@Component
public class JdbcTemplateDemo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Find all data in table 'db_tabs' and mapping the results as a {@link List}.
     * @return  {@link List} of {@link DbTag}
     */
    public List<DbTag> findAll() {
        return jdbcTemplate.query("SELECT * FROM db_tags", (resultSet, i) -> {
            Long id = resultSet.getLong("id");
            String tagName = resultSet.getString("tag_name");
            Long versions = resultSet.getLong("versions");
            Date createTime = resultSet.getTimestamp("create_time");
            return new DbTag(id, tagName, versions, createTime);
        });
    }

    /**
     * Insert a row into db_tags without transaction, the generated key will be filled in the source object.
     * @param dbTag The data to insert.
     */
    public void insert(DbTag dbTag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO db_tags(tag_name, versions, create_time) "
                    + "VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dbTag.getTagName());
            ps.setLong(2, dbTag.getVersions());
            ps.setDate(3, new java.sql.Date(dbTag.getCreateTime().getTime()));
            return ps;
        }, keyHolder);
        dbTag.setId(keyHolder.getKey().longValue());
    }

    /**
     * Delete a row from db_tags by primary key "id" without transaction.
     * @param id  The id of row.
     */
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM db_tags WHERE id = ?", id);
    }

    /**
     * Update a row from db_tags by primary key "id" without transaction.
     * @param dbTag  The object to update.
     */
    public void update(DbTag dbTag) {
        jdbcTemplate.update("UPDATE db_tags SET tag_name=?, versions=?, create_time=? WHERE id=?",
                new Object[]{dbTag.getTagName(), dbTag.getVersions(), dbTag.getCreateTime(), dbTag.getId()},
                new int[]{Types.VARCHAR, Types.BIGINT, Types.DATE, Types.BIGINT});
    }

    /**
     * This method is for testing the validity of transaction, the first insertion will be successful and
     * a RuntimeException will be thrown before the second insertion. Finally, neither the first
     * nor the second would make affect in DataBase.
     *
     * <br/>
     *
     * This method is just for transaction, so there is no need to return generated key.
     *
     * @param first  For first operation, which will not affect DataBase because of transaction.
     * @param second  For second operation, which will never be executed.
     */
    @Transactional
    public void insertTwice(DbTag first, DbTag second) {
        jdbcTemplate.update("INSERT INTO db_tags(tag_name, versions, create_time) "
                        + "VALUES(?, ?, ?)",
                new Object[]{first.getTagName(), first.getVersions(), first.getCreateTime()},
                new int[]{Types.VARCHAR, Types.BIGINT, Types.DATE});
        //Generate a RuntimeException
        int i = 1 / 0;
        jdbcTemplate.update("INSERT INTO db_tags(tag_name, versions, create_time) "
                        + "VALUES(?, ?, ?)",
                new Object[]{second.getTagName(), second.getVersions(), second.getCreateTime()},
                new int[]{Types.VARCHAR, Types.BIGINT, Types.DATE});

    }
}
