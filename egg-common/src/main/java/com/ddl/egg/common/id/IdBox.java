package com.ddl.egg.common.id;

import java.util.Date;

/**
 * ID盒子
 */
public class IdBox {
    /**
     * 时间戳
     */
    private long timestamp;
    /**
     * 保留号
     */
    private long reserveId;
    /**
     * 机器号
     */
    private long workerId;
    /**
     * 序列号
     */
    private long sequence;
    /**
     * 时间
     */
    private Date time;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getReserveId() {
        return reserveId;
    }

    public void setReserveId(long reserveId) {
        this.reserveId = reserveId;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IdBox{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", reserveId=").append(reserveId);
        sb.append(", workerId=").append(workerId);
        sb.append(", sequence=").append(sequence);
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }
}
