package com.ddl.egg.common.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * ID生成器
 * <p>
 * 参考Twitter-Snowflake算法实现
 * <p>
 * (a) Simba系统Id构成: 1位符号位 + 41位的时间前缀 + 6位保留位数（64） + 6位机器号码（64） + 10位毫秒自增序列（1024）(10位不够用时强制得到新的时间前缀)
 * 注意这里进行了小改动: snowkflake是5位的datacenter加5位的机器id; 这里变成使用10位的机器id
 * (b) 对系统时间的依赖性非常强，需关闭ntp的时间同步功能。当检测到ntp时间调整后，将会拒绝分配id
 * <p>
 */
public class IdWorker {
    private static final Logger logger = LoggerFactory.getLogger(IdWorker.class);
    private final static long twepoch = 1514649600000L;//初始时时间点2018-01-01

    private final static long sequenceBits = 10L;// 自增序列位数
    private final static long workerIdBits = 6L; // 机器号位数
    private final static long reserveIdBits = 6L; // 保留号位数
    private final static long maxSequence = -1L ^ -1L << sequenceBits; //最大序列号
    private final static long maxWorkerId = -1L ^ -1L << workerIdBits; //最大机器号
    private final static long maxReserveId = -1L ^ -1L << reserveIdBits; //最大保留号
    private final static long workerIdShift = sequenceBits; //机器ID偏移位数
    private final static long reserveIdShift = sequenceBits + workerIdBits; //保留ID偏移位数
    private final static long timestampLeftShift = sequenceBits + workerIdBits + reserveIdBits; //时间戳偏移位数
    private int workerId; //机器号
    private long sequence = 0L;//序列初始值
    private long reserveId = 0L;//保留号初始值
    private long lastTimestamp = -1L;//最后时间戳


    public IdWorker(final int workerId) {
        super();
        if (workerId > this.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
        }
        this.workerId = workerId;
    }

    /**
     * 解析ID
     *
     * @param id
     * @return
     */
    public static IdBox analyze(long id) {
        long timestamp = (id >> timestampLeftShift) + twepoch;
        long reserveId = id >> reserveIdShift & maxReserveId;
        long workerId = id >> workerIdShift & maxWorkerId;
        long sequence = id & maxSequence;
        IdBox idBox = new IdBox();
        idBox.setTimestamp(timestamp);
        idBox.setReserveId(reserveId);
        idBox.setWorkerId(workerId);
        idBox.setSequence(sequence);
        idBox.setTime(new Date(timestamp));
        return idBox;
    }

    /**
     * 获取分布式ID
     *
     * @return
     */
    public synchronized long nextId() {
        return nextId(0L);
    }

    /**
     * 获取分布式ID
     *
     * @param number
     * @return
     */
    public synchronized long nextId(Long number) {
        reserveId = number % (maxReserveId + 1);
        long timestamp = System.currentTimeMillis();
        // 如果上一个timestamp与新产生的相等，则sequence加1; 对新的timestamp，sequence从0开始
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & this.maxSequence;
            if (this.sequence == 0) {
                logger.info("sequenceMask超过最大限制：{}" + maxSequence);
                timestamp = this.tilNextMillis(this.lastTimestamp);// 重新生成timestamp
            }
        } else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id for "+(this.lastTimestamp - timestamp)+" milliseconds");
        }
        this.lastTimestamp = timestamp;
        long nextId = ((timestamp - twepoch << timestampLeftShift)) | (this.reserveId << this.reserveIdShift) | (this.workerId << this.workerIdShift) | (this.sequence);
        return nextId;
    }

    /**
     * 等待下一个毫秒的到来, 保证返回的毫秒数在参数lastTimestamp之后
     */
    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }
}
