package de.tuberlin.dima.schubotz.fse;

import eu.stratosphere.api.java.DataSet;
import eu.stratosphere.api.java.ExecutionEnvironment;
import eu.stratosphere.api.java.io.TextInputFormat;
import eu.stratosphere.api.java.operators.DataSource;
import eu.stratosphere.api.java.typeutils.BasicTypeInfo;
import eu.stratosphere.core.fs.Path;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.util.HashMap;
import java.util.Map;

/**
 * Performs the queries for the NTCIR-Math11-Workshop 2014 fully automated.
 *
 */

public class MainProgram {
    // set up execution environment
    static final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
    /**
     * The overall maximal results that can be returned per query.
     */
    public final static int MaxResultsPerQuery = 3000;

    /**
     * The Constant LUCENE.
     */
    public static final boolean LUCENE = false;

    /**
     * The Constant RECORD_WORD.
     */
    public static final int RECORD_WORD = 0;

    /**
     * The Constant RECORD_VARIABLE.
     */
    public static final int RECORD_VARIABLE = 1;

    /**
     * The Constant RECORD_MATCH.
     */
    public static final int RECORD_MATCH = 2;
    public static final Map<String, String> QueryDesc = new HashMap<>();
    private static final String DOCUMENT_SEPARATOR = "</ARXIVFILESPLIT> \n";
    public static final String RECOD_TYPE = "RECORD_TYPE";
    public static Map<String, String> TeXQueries = new HashMap<String, String>();
    /**
     * The Constant LOG.
     */
    private static final Log LOG = LogFactory.getLog(MainProgram.class);
    /**
     * The number of parallel tasks to be executed
     */
    static int noSubTasks;
    /**
     * The Input XML-File that contains the document collection
     */
    static String docsInput;
    /**
     * The Input CSV-file with the human evaluation
     */
    static String queryInput;
    /**
     * The Output XML file with the calculated results
     */
    static String output;

    protected static void parseArg(String[] args)  {
        // parse job parameters
        noSubTasks = (args.length > 0 ? Integer.parseInt(args[0])
                : 16);
        docsInput = (args.length > 1 ? args[1]
                : "file:///mnt/ntcir-math/testdata/test10000.xml");
        queryInput = (args.length > 2 ? args[2]
                : "file:///mnt/ntcir-math/queries/fquery.xml");
        output = (args.length > 3 ? args[3]
                : "file:///mnt/ntcir-math/test-output/testout-"+ System.currentTimeMillis() +".xml");
    }
    public static void main(String[] args) throws Exception {
        parseArg(args);
        ConfigurePlan();
        env.execute("Mathosphere");
    }
    public static ExecutionEnvironment getExecutionEnvironment() throws Exception {
        return env;
    }
    protected static void ConfigurePlan() throws XPathExpressionException, ParserConfigurationException {
        TextInputFormat format = new TextInputFormat(new Path(docsInput));
        format.setDelimiter(DOCUMENT_SEPARATOR);
        DataSet<String> rawArticleText = new DataSource<String>(env, format, BasicTypeInfo.STRING_TYPE_INFO);
        TextInputFormat formatQueries = new TextInputFormat(new Path(docsInput));
        formatQueries.setDelimiter("</topics>"); //Do not split topics
        DataSet<String> rawQueryText = new DataSource<String>(env, format, BasicTypeInfo.STRING_TYPE_INFO);
        DataSet<Query> queryDataSet= rawQueryText.flatMap(new QueryMapper());
        queryDataSet.print();
    }


}
