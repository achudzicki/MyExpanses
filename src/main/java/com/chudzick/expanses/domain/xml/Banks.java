package com.chudzick.expanses.domain.xml;

public enum Banks {
    PKO_BP("PKO_BP");

    private String schemaName;

    Banks(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaName() {
        return schemaName;
    }
}
