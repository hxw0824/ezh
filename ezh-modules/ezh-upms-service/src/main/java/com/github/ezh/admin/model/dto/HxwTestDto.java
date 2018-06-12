package com.github.ezh.admin.model.dto;

import com.github.ezh.admin.model.entity.SysHxwTest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * @author solor
 * @date 2017/11/5
 */
@Data
@Log4j
@AllArgsConstructor
@NoArgsConstructor
public class HxwTestDto extends SysHxwTest {
    private String testConcat;

    public String getTestConcat() {
        return testConcat;
    }

    public void setTestConcat(String testConcat) {
        this.testConcat = testConcat;
    }
}
