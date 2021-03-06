package com.zhj.test.model;

public class Cnip {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cnip.start
     *
     * @mbg.generated Tue Dec 15 09:44:19 CST 2020
     */
    private Long start;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cnip.count
     *
     * @mbg.generated Tue Dec 15 09:44:19 CST 2020
     */
    private Integer count;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cnip.start
     *
     * @return the value of cnip.start
     *
     * @mbg.generated Tue Dec 15 09:44:19 CST 2020
     */
    public Long getStart() {
        return start;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cnip.start
     *
     * @param start the value for cnip.start
     *
     * @mbg.generated Tue Dec 15 09:44:19 CST 2020
     */
    public void setStart(Long start) {
        this.start = start;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cnip.count
     *
     * @return the value of cnip.count
     *
     * @mbg.generated Tue Dec 15 09:44:19 CST 2020
     */
    public Integer getCount() {
        return count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cnip.count
     *
     * @param count the value for cnip.count
     *
     * @mbg.generated Tue Dec 15 09:44:19 CST 2020
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    public Cnip(Long start, Integer count) {
        this.start = start;
        this.count = count;
    }
    //ip区间
    public boolean contains(long ipValue){
        return ipValue >= start && ipValue < start + count;
    }
}