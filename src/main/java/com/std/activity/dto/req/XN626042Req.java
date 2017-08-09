package com.std.activity.dto.req;

/**
 * 修改关键词
 * @author: asus 
 * @since: 2017年7月12日 下午2:45:25 
 * @history:
 */
public class XN626042Req {

    // 关键词编号 （必填）
    private String code;

    // 关键词(必填)
    private String word;

    // 权重(必填)
    private String weight;

    // 作用等级(必填)
    private String level;

    // 反应(必填)
    private String reaction;

    // 更新人(必填)
    private String updater;

    // 备注(选填)
    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
