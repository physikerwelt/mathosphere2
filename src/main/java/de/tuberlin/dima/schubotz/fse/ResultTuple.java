package de.tuberlin.dima.schubotz.fse;

import eu.stratosphere.api.java.DataSet;
import eu.stratosphere.api.java.tuple.Tuple3;

/**
 * Schema according to @url{https://svn.mathweb.org/repos/NTCIR-Math/topics/ntcir11/lib/NTCIR11-results.rnc}
 * result = element result {id.att & for.att & runtime.att & hit+}
 */
public class ResultTuple extends Tuple3<String, Double, explicitDataSet<HitTuple>> {
    public DataSet<HitTuple> getHits() {
        return getField(fields.hits.ordinal());
    }

    public void setHits(DataSet<HitTuple> hits) {
        setField(hits, fields.hits.ordinal());
    }

    public String getFor() {
        return getField(fields.aFor.ordinal());
    }

    public void setFor(String aFor) {
        setField(aFor, fields.aFor.ordinal());
    }

    public Double getRuntime() {
        return getField(fields.runtime.ordinal());
    }

    public void setRuntime(Double runtime) {
        setField(runtime, fields.runtime.ordinal());
    }

    public enum fields {
        aFor, runtime, hits
    }

}