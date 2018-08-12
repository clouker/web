package org.clc.pojo;

public  class DBInfo {

    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public static class Table {

        private String name;

        private String encoding;

        private Table(String name, String encoding){
            this.name = name;
            this.encoding = encoding;
        }
        public Table(String name){
            this(name,"");
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }
    }
}


