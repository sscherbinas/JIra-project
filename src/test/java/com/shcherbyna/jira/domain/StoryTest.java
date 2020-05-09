package com.shcherbyna.jira.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.shcherbyna.jira.web.rest.TestUtil;

public class StoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Story.class);
        Story story1 = new Story();
        story1.setId(1L);
        Story story2 = new Story();
        story2.setId(story1.getId());
        assertThat(story1).isEqualTo(story2);
        story2.setId(2L);
        assertThat(story1).isNotEqualTo(story2);
        story1.setId(null);
        assertThat(story1).isNotEqualTo(story2);
    }
}
