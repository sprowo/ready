package com.prowo.ymlchain.json;

public class JsonBuilder {
    private StringBuilder sb;
    private String contain = "\"";

    public JsonBuilder() {
        sb = new StringBuilder("{");
    }

    public JsonBuilder(String contain) {
        this.contain = contain;
        sb = new StringBuilder("{");
    }

    public String toString() {
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    /**
     * 增加数组开始
     *
     * @param key 数组的key
     * @return
     */
    public JsonBuilder arrayStart(String key) {
        sb.append(contain).append(key).append(contain).append(":[");
        return this;
    }


    /**
     * 增加数组中的块
     *
     * @param jb 数组的块
     * @return
     */
    public JsonBuilder addBlock(JsonBuilder jb) {
        sb.append(jb.toString()).append(",");
        return this;
    }

    /**
     * 增加数组结束
     *
     * @return
     */
    public JsonBuilder arrayEnd() {
        sb.deleteCharAt(sb.length() - 1);
        sb.append("],");
        return this;
    }

    /**
     * 增加json键值对
     *
     * @return
     */
    public JsonBuilder kv(String key, String val) {
        sb.append(contain).append(key).append(contain).append(":").append(contain)
                .append(val == null ? "" : val.toString()).append(contain).append(",");
        return this;
    }

    public JsonBuilder kv(String key, JsonBuilder val) {
        sb.append(contain).append(key).append(contain).append(":").append(val == null ? "" : val.toString())
                .append(",");
        return this;
    }

    public JsonBuilder kv(String key, boolean val) {
        sb.append(contain).append(key).append(contain).append(":").append(contain).append(val).append(contain)
                .append(",");
        return this;
    }

    public JsonBuilder kv(String key, int val) {
        sb.append(contain).append(key).append(contain).append(":").append(contain).append(val).append(contain)
                .append(",");
        return this;
    }

    public JsonBuilder kv(String key, long val) {
        sb.append(contain).append(key).append(contain).append(":").append(contain).append(val).append(contain)
                .append(",");
        return this;
    }

    public JsonBuilder kv(String key, float val) {
        sb.append(contain).append(key).append(contain).append(":").append(contain).append(val).append(contain)
                .append(",");
        return this;
    }

    public JsonBuilder kv(String key, double val) {
        sb.append(contain).append(key).append(contain).append(":").append(contain).append(val).append(contain)
                .append(",");
        return this;
    }

    public static void main(String[] args) {
        JsonBuilder jb = new JsonBuilder().kv("a", "111").kv("b", "111111");
        jb.arrayStart("ccc");

        jb.addBlock(new JsonBuilder().kv("a", "111").kv("b", "111111"));
        jb.addBlock(new JsonBuilder().kv("a", "111").kv("b", "111111"));
        jb.arrayEnd();
        jb.arrayStart("ccc");
        jb.addBlock(new JsonBuilder().kv("a", "111").kv("b", "111111"));
        jb.addBlock(new JsonBuilder().kv("a", "111").kv("b", "111111"));
        jb.arrayEnd();
        System.out.println(jb.toString());
    }
}
