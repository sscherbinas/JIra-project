package com.shcherbyna.jira.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.shcherbyna.jira.web.rest.TestUtil;

public class TestCaseTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCase.class);
        TestCase testCase1 = new TestCase();
        testCase1.setId(1L);
        TestCase testCase2 = new TestCase();
        testCase2.setId(testCase1.getId());
        assertThat(testCase1).isEqualTo(testCase2);
        testCase2.setId(2L);
        assertThat(testCase1).isNotEqualTo(testCase2);
        testCase1.setId(null);
        assertThat(testCase1).isNotEqualTo(testCase2);
    }
}
