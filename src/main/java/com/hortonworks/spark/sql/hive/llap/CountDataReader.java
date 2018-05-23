package com.hortonworks.spark.sql.hive.llap;

import java.io.IOException;
import org.apache.spark.sql.vectorized.ColumnarBatch;
import org.apache.spark.sql.execution.vectorized.OnHeapColumnVector;
import org.apache.spark.sql.sources.v2.reader.DataReader;
import org.apache.spark.sql.vectorized.ColumnVector;
import org.apache.spark.sql.types.DataTypes;

public class CountDataReader implements DataReader<ColumnarBatch> {
  private long numRows;

  public CountDataReader(long numRows) {
    this.numRows = numRows;
  }

  @Override public boolean next() throws IOException {
    if(numRows < 0) {
      throw new IOException();
    }
    return numRows > 0;
  }

  @Override public ColumnarBatch get() {
    //int size = (numRows >= 1000) ? 1000 : (int) numRows;
    OnHeapColumnVector vector = new OnHeapColumnVector(1, DataTypes.LongType);
    //for(int i = 0; i < size; i++) {
      vector.putLong(0, numRows);
    //}
    numRows = 0;
    ColumnarBatch batch = new ColumnarBatch(new ColumnVector[] {vector});
    batch.setNumRows(1);
    return batch;
  }

  @Override
  public void close() {
    //NOOP
  }
}
