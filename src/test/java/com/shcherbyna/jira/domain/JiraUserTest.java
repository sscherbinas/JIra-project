package com.shcherbyna.jira.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.shcherbyna.jira.web.rest.TestUtil;

public class JiraUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JiraUser.class);
        JiraUser jiraUser1 = new JiraUser();
        jiraUser1.setId(1L);
        JiraUser jiraUser2 = new JiraUser();
        jiraUser2.setId(jiraUser1.getId());
        assertThat(jiraUser1).isEqualTo(jiraUser2);
        jiraUser2.setId(2L);
        assertThat(jiraUser1).isNotEqualTo(jiraUser2);
        jiraUser1.setId(null);
        assertThat(jiraUser1).isNotEqualTo(jiraUser2);
    }
}
