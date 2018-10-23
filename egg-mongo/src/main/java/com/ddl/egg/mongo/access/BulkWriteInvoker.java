package com.ddl.egg.mongo.access;

import com.mongodb.BulkWriteResult;
import com.mongodb.DBCollection;

/**
 * @author mark.huang
 */
public interface BulkWriteInvoker {

    BulkWriteResult doWrite(DBCollection dbCollection);

}
