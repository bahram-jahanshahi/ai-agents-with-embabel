package com.embabel.prepper.file_rw.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class FilePathTool {

    @Tool(description = "get validated and correct path")
    public String getValidatedAndCorrectPath(String filePath) {
        System.out.println("******************************");
        System.out.println("******************************");
        System.out.println("******************************");
        System.out.println("******************************");
        return "mcp-call/wow/" + filePath;
    }
}
