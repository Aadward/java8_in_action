package com.syh.springboot.jdbc.demo;

import java.util.Date;
import java.util.List;

import com.syh.springboot.jdbc.demo.model.DbTag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Main.class)
@RunWith(SpringRunner.class)
public class JdbcTemplateDemoTest {

    @Autowired
    private JdbcTemplateDemo template;

    @Test
    @Transactional
    @Rollback
    public void computedemo_jdbctemplate_insert() {
        DbTag dbTag = new DbTag("test", 123L, new Date());
        template.insert(dbTag);
        Assert.assertNotNull(dbTag.getId());
    }

    @Test
    public void computedemo_jdbctemplate_select_empty() {
        List<DbTag> dbTags = template.findAll();
        Assert.assertEquals(0, dbTags.size());
    }

    @Test
    @Transactional
    @Rollback
    public void computedemo_jdbctemplate_insert_then_select() {
        DbTag dbTag = new DbTag("test", 123L, new Date());
        template.insert(dbTag);

        List<DbTag> dbTags = template.findAll();
        Assert.assertEquals(1, dbTags.size());
    }

    @Test
    @Transactional
    @Rollback
    public void computedemo_jdbctemplate_insert_delete() {
        DbTag dbTag = new DbTag("test", 123L, new Date());
        template.insert(dbTag);

        List<DbTag> afterInsert = template.findAll();
        Assert.assertEquals(1, afterInsert.size());

        template.deleteById(dbTag.getId());
        List<DbTag> afterDelete = template.findAll();
        Assert.assertEquals(0, afterDelete.size());
    }


    @Test
    @Transactional
    @Rollback
    public void computedemo_jdbctemplate_insert_update() {
        DbTag dbTag = new DbTag("test", 123L, new Date());
        template.insert(dbTag);

        dbTag.setTagName("test1");
        template.update(dbTag);

        List<DbTag> afterUpdate = template.findAll();
        Assert.assertEquals(1, afterUpdate.size());

        DbTag found = afterUpdate.get(0);
        Assert.assertEquals("test1", found.getTagName());
    }

    @Test
    public void computedemo_jdbctemplate_fail_on_transaction() {
        DbTag dbTag1 = new DbTag("test1", 123L, new Date());
        DbTag dbTag2 = new DbTag("test2", 123L, new Date());

        try {
            template.insertTwice(dbTag1, dbTag2);
        } catch (RuntimeException e) {
            // must thrown a runtime exception, and this exception will trigger rollback by transaction
        }
        List<DbTag> dbTags = template.findAll();
        Assert.assertEquals(0, dbTags.size());
    }
}

