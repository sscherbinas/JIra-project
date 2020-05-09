package com.shcherbyna.jira.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.shcherbyna.jira.web.rest.TestUtil;

public class DefectTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Defect.class);
        Defect defect1 = new Defect();
        defect1.setId(1L);
        Defect defect2 = new Defect();
        defect2.setId(defect1.getId());
        assertThat(defect1).isEqualTo(defect2);
        defect2.setId(2L);
        assertThat(defect1).isNotEqualTo(defect2);
        defect1.setId(null);
        assertThat(defect1).isNotEqualTo(defect2);
    }
}
