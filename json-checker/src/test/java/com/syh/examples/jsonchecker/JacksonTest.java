package com.syh.examples.jsonchecker;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class JacksonTest {

    private static ObjectMapper ob;

    @BeforeClass
    public static void init() {
        ob = new ObjectMapper();
    }

    @Test
    public void ruleCheck_jackson() throws Exception {
        String oneCondition = "{\n" +
                "  \"conditions\": {\n" +
                "    \"and\": [\n" +
                "      {\n" +
                "        \"field\": \"$.user_action\",\n" +
                "        \"target\": \"join\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"window\": {\n" +
                "    \"windowMinute\": 5,\n" +
                "    \"limit\": 2\n" +
                "  }\n" +
                "}";
        Rule rule = ob.readValue(oneCondition, Rule.class);
        assertThat(rule.getConditions().values()).size().isEqualTo(1);
        assertThat(rule.getWindow()).isNotNull();
    }

    @Test(expected = JsonMappingException.class)
    public void ruleCheck_jackson_noCondition() throws Exception {
        String noCondition = "{}";
        ob.readValue(noCondition, Rule.class);
    }

    @Test(expected = JsonMappingException.class)
    public void ruleCheck_jackson_emptyCondition() throws Exception {
        String emptyCondition = "{\n" +
                "  \"conditions\": {\n" +
                "    \"and\": [\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        ob.readValue(emptyCondition, Rule.class);
    }

    @Test(expected = JsonMappingException.class)
    public void ruleCheck_jackson_badConditionField() throws Exception {
        String badConditionField = "{\n" +
                "  \"conditions\": {\n" +
                "    \"and\": [\n" +
                "      {\n" +
                "        \"field\": \"user_action\",\n" +
                "        \"target\": \"join\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"window\": {\n" +
                "    \"windowMinute\": 5,\n" +
                "    \"limit\": 2\n" +
                "  }\n" +
                "}";
        ob.readValue(badConditionField, Rule.class);
    }

}
