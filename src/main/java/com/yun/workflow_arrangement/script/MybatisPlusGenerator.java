package com.yun.workflow_arrangement.script;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;

/**
 *
 *
 * @author raoliwen
 * @date 2026/01/28
 */
public class MybatisPlusGenerator {
    public static void main(String[] args) {
        String projectDir = System.getProperty("user.dir");
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/traction_agent?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true",
                        "root",
                        "12345678")
                .globalConfig(builder -> builder
                        .author("raoliwen")
                        .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java")
                        .commentDate("yyyy-MM-dd")
                )
                .packageConfig(builder -> builder
                        .parent("com.xiaohongshu.governance.traction_agent")
                        .entity("infra.entity")
                        .mapper("infra.mapper")
                        .service("infra.service")
                        .serviceImpl("infra.service.impl")
                        .pathInfo(java.util.Collections.singletonMap(
                                com.baomidou.mybatisplus.generator.config.OutputFile.xml,
                                Paths.get(projectDir, "src/main/resources/com/xiaohongshu/governance/traction_agent/infra/mapper")
                                        .toString()
                        ))
                )
                .strategyConfig(builder -> builder
                        .addInclude("prompt_messages","ai_chat_messages")
                        .controllerBuilder().disable()
                        .serviceBuilder().enableFileOverride()
                        .entityBuilder().enableFileOverride().enableTableFieldAnnotation().enableLombok()
                        .mapperBuilder().enableFileOverride()
                )
                .templateConfig(builder -> builder.disable(com.baomidou.mybatisplus.generator.config.TemplateType.CONTROLLER))
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
