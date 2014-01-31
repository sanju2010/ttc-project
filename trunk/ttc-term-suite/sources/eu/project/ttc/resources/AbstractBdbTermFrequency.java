package eu.project.ttc.resources;

import com.sleepycat.je.*;
import eu.project.ttc.models.Context;
import eu.project.ttc.types.TermAnnotation;
import org.apache.commons.lang.mutable.MutableDouble;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by sebastian on 31/01/14.
 */
public abstract class AbstractBdbTermFrequency<T extends TermAnnotation> implements ITermFrequency<T> {

    /**
     * Reusable int buffer
     */
    protected final ByteBuffer buffer = ByteBuffer.allocate(4);

    /**
     * Reusable double buffer
     */
    protected final ByteBuffer doubleBuffer = ByteBuffer.allocate(8);

    /**
     * Underlying embedded database
     */
    protected final Database database;

    public AbstractBdbTermFrequency(String dbName, Environment environment) throws ResourceInitializationException {
        try {
            DatabaseConfig databaseConfig = new DatabaseConfig();
            databaseConfig.setAllowCreate(true);
            databaseConfig.setSortedDuplicates(false);
            databaseConfig.setTransactional(false);
            safeRemove(environment, dbName);
            database = environment.openDatabase(null, dbName, databaseConfig);
        } catch (DatabaseException e) {
            throw new ResourceInitializationException(e);
        }
    }

    private void safeRemove(Environment environment, String dbName) throws DatabaseException {
        try {
            environment.removeDatabase(null, dbName);
        } catch (DatabaseNotFoundException e) {
        }
    }

    //----------------------------------- Implemented methods from ITermFrequency interface

    @Override
    public String getCategory(String term) {
        return getStrValue("\0" + term + "\0-\0cat\0");
    }

    @Override
    public int getTermFrequency(String term) {
        try {
            return getIntValue(fromString(term));
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getFormFrequency(String term, String form) {
        try {
            return getIntValue(fromString("[" + term + "-" + form + "]"));
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setCoOccurrences(String term, String coTerm, double occurrences, int mode) {
        new BdbContext<T>(term, this).setCoOccurrences(coTerm, occurrences, mode);
    }

    @Override
    public void addCoOccurrences(String term, String context) {
        new BdbContext<T>(term, this).setCoOccurrences(context, 1.0, Context.ADD_MODE);
    }

    @Override
    public Context getContext(String term) {
        return new BdbContext<T>(term, this);
    }

    @Override
    public Map<String, Context> getContexts() {
        TreeMap<String, Context> res = new TreeMap<String, Context>();
        for (String k : getTerms()) {
            res.put(k, new BdbContext<T>(k, this));
        }
        return res;
    }

    @Override
    public void load(DataResource aData) throws ResourceInitializationException {
    }

    @Override
    public Set<String> getTerms() {
        Cursor cursor = null;
        try {
            cursor = database.openCursor(null, null);
            DatabaseEntry key = new DatabaseEntry();
            DatabaseEntry val = new DatabaseEntry();
            HashSet<String> result = new HashSet<String>();
            while (cursor.getNext(key, val, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                String retKey = new String(key.getData(), "UTF-8");
                if (!retKey.matches("^[\0(\\[].*")) {
                    result.add(retKey);
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            safeClose(cursor);
        }
    }

    @Override
    public Set<String> getForms(String entry) {
        Cursor cursor = null;
        try {
            cursor = database.openCursor(null, null);
            String strKey = "[" + entry + "-";
            DatabaseEntry key = fromString(strKey);
            DatabaseEntry val = new DatabaseEntry();
            OperationStatus status = cursor.getSearchKeyRange(key, val, null);
            if (status != OperationStatus.SUCCESS)
                return Collections.emptySet();
            HashSet<String> result = new HashSet<String>();
            while (cursor.getNext(key, val, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                String retKey = new String(key.getData(), "UTF-8");
                if (retKey.startsWith(strKey)) {
                    result.add(retKey.substring(strKey.length() + 1, retKey.length() - 1));
                } else {
                    break;
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            safeClose(cursor);
        }
    }

    @Override
    public void close() {
        try {
            database.close();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    protected String addGeneric(TermAnnotation annotation) {
        String term = annotation.getLemma().toLowerCase()
                .replaceAll("\\s+", " ").trim();
        String form = annotation.getCoveredText().trim();
        if (this.allow(term)) {

            increment(term);
            increment("[" + term + "-" + form + "]");
            setValue("\0" + term + "\0-\0cat\0", annotation.getCategory());

            return term;
        } else {
            return null;
        }
    }

    // ------------------------------------ Database util methods

    protected void setValue(String strKey, String strVal) {
        try {
            DatabaseEntry key = fromString(strKey);
            DatabaseEntry val = fromString(strVal);
            database.put(null, key, val);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void setValue(DatabaseEntry key, double dVal) {
        try {
            DatabaseEntry data = new DatabaseEntry();
            doubleBuffer.clear();
            doubleBuffer.putDouble(dVal);
            data.setData(doubleBuffer.array());
            database.put(null, key, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void increment(String strKey) {
        try {
            DatabaseEntry key = fromString(strKey);
            int i = getIntValue(key) + 1;
            DatabaseEntry data = new DatabaseEntry();
            buffer.clear();
            buffer.putInt(i);
            data.setData(buffer.array());
            database.put(null, key, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected int getIntValue(DatabaseEntry key) throws DatabaseException {
        DatabaseEntry data = new DatabaseEntry();
        OperationStatus status = database.get(null, key, data, LockMode.DEFAULT);
        int res = 0;
        if (status == OperationStatus.SUCCESS) {
            ByteBuffer b = ByteBuffer.wrap(data.getData());
            res = b.getInt();
        }
        return res;
    }

    protected double getDoubleValue(DatabaseEntry key) throws DatabaseException {
        DatabaseEntry data = new DatabaseEntry();
        OperationStatus status = database.get(null, key, data, LockMode.DEFAULT);
        double res = 0.0;
        if (status == OperationStatus.SUCCESS) {
            ByteBuffer b = ByteBuffer.wrap(data.getData());
            res = b.getDouble();
        }
        return res;
    }

    protected DatabaseEntry fromString(String str) {
        try {
            return new DatabaseEntry(str.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getStrValue(String strKey) {
        try {
            DatabaseEntry data = new DatabaseEntry();
            database.get(null, fromString(strKey), data, LockMode.DEFAULT);
            return new String(data.getData(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean allow(String term) {

        if (term == null) {
            return false;
        } else if (term.length() <= 2) {
            return false;
        } else if (term.length() > 50) {
            return false;
        } else if (term.startsWith("http:") || term.startsWith("www")) {
            return false;
        }

        char ch = term.charAt(0);
        int type = Character.getType(ch);
        Character.UnicodeBlock unicode = Character.UnicodeBlock.of(ch);
        if (type == Character.LOWERCASE_LETTER) {
            return true;
        } else if (unicode == Character.UnicodeBlock.CYRILLIC) {
            return true;
        } else if (unicode == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
            return true;
        } else {
            return false;
        }
    }

    protected void safeClose(Cursor cursor) {
        if (cursor != null)
            try {
                cursor.close();
            } catch (DatabaseException e) {
            }
    }

    //----------------------------- Context vectors

    /**
     * Inner class, implements contexts based on the underlying database.
     * Created by sebastian on 30/01/14.
     */
    protected static class BdbContext<T extends TermAnnotation> extends Context {

        private final String forThisKey;

        private final AbstractBdbTermFrequency<T> enclosing;

        public BdbContext(String parentKey, AbstractBdbTermFrequency<T> parent) {
            forThisKey = parentKey;
            enclosing = parent;
        }

        public void setCoOccurrences(String term, double coOccurrences, int mode) {
            if (!enclosing.allow(term))
                return;

            try {
                DatabaseEntry key = enclosing.fromString("(" + forThisKey + "," + term + ")");
                double currentVal = enclosing.getDoubleValue(key);
                if (mode == DEL_MODE) {
                    enclosing.setValue(key, coOccurrences);
                } else if (mode == ADD_MODE) {
                    enclosing.setValue(key, currentVal + coOccurrences);
                } else if (mode == MAX_MODE && coOccurrences > currentVal) {
                    enclosing.setValue(key, coOccurrences);
                } else if (mode == MIN_MODE && coOccurrences < currentVal) {
                    enclosing.setValue(key, coOccurrences);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Set<String> getCoocurringTerms() {
            Cursor cursor = null;
            try {
                cursor = enclosing.database.openCursor(null, null);
                String strKey = "(" + forThisKey + ",";
                DatabaseEntry key = enclosing.fromString(strKey);
                DatabaseEntry val = new DatabaseEntry();
                OperationStatus status = cursor.getSearchKeyRange(key, val, null);
                if (status != OperationStatus.SUCCESS)
                    return Collections.emptySet();
                HashSet<String> result = new HashSet<String>();
                while (cursor.getNext(key, val, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                    String retKey = new String(key.getData(), "UTF-8");
                    if (retKey.startsWith(strKey)) {
                        result.add(retKey.substring(strKey.length() + 1, retKey.length() - 1));
                    } else {
                        break;
                    }
                }
                return result;
            } catch (Exception e) {
                throw new RuntimeException(e);

            } finally {
                enclosing.safeClose(cursor);
            }
        }

        protected Map<String, MutableDouble> sort() {
            final Map<String, MutableDouble> mVector = getMutableOccurrenceVector();
            Map<String, MutableDouble> occurrences = new TreeMap<String, MutableDouble>(new ContextComparator() {
                @Override
                public int compare(String sourceKey, String targetKey) {
                    MutableDouble sourceValue = mVector.get(sourceKey);
                    MutableDouble targetValue = mVector.get(targetKey);
                    double source = sourceValue == null ? 0.0 : sourceValue.doubleValue();
                    double target = targetValue == null ? 0.0 : targetValue.doubleValue();
                    if (source < target) {
                        return 1;
                    } else if (source > target) {
                        return -1;
                    } else {
                        return sourceKey.compareTo(targetKey);
                    }
                }
            });
            occurrences.putAll(mVector);
            return occurrences;
        }

        public Map<String, Double> getOccurrenceVector() {
            Cursor cursor = null;
            try {
                cursor = enclosing.database.openCursor(null, null);
                String strKey = "(" + forThisKey + ",";
                DatabaseEntry key = enclosing.fromString(strKey);
                DatabaseEntry val = new DatabaseEntry();
                OperationStatus status = cursor.getSearchKeyRange(key, val, null);
                if (status != OperationStatus.SUCCESS)
                    return Collections.emptyMap();

                HashMap<String, Double> result = new HashMap<String, Double>();
                while (cursor.getNext(key, val, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                    String retKey = new String(key.getData(), "UTF-8");
                    if (retKey.startsWith(strKey)) {
                        ByteBuffer dBuff = ByteBuffer.wrap(val.getData());
                        result.put(retKey.substring(strKey.length() + 1, retKey.length() - 1), new Double(dBuff.getDouble()));
                    } else {
                        break;
                    }
                }
                return result;
            } catch (Exception e) {
                throw new RuntimeException(e);

            } finally {
                enclosing.safeClose(cursor);
            }
        }

        public Map<String, MutableDouble> getMutableOccurrenceVector() {
            Cursor cursor = null;
            String lastError = "null";
            String strKey = "(" + forThisKey + ",";
            try {
                cursor = enclosing.database.openCursor(null, null);
                DatabaseEntry key = enclosing.fromString(strKey);
                DatabaseEntry val = new DatabaseEntry();
                OperationStatus status = cursor.getSearchKeyRange(key, val, null);
                if (status != OperationStatus.SUCCESS)
                    return Collections.emptyMap();

                HashMap<String, MutableDouble> result = new HashMap<String, MutableDouble>();
                while (cursor.getNext(key, val, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                    String retKey = new String(key.getData(), "UTF-8");
                    lastError = retKey;
                    if (retKey.startsWith(strKey)) {
                        ByteBuffer dBuff = ByteBuffer.wrap(val.getData());
                        result.put(retKey.substring(strKey.length() + 1, retKey.length() - 1), new MutableDouble(dBuff.getDouble()));
                    } else {
                        break;
                    }
                }
                return result;
            } catch (Exception e) {
                System.out.println("Last error on : " + lastError);
                throw new RuntimeException(e);

            } finally {
                enclosing.safeClose(cursor);
            }
        }

        public double getOccurrences(String coTerm) {
            if (!enclosing.allow(coTerm))
                return 0.0;
            try {
                return enclosing.getDoubleValue(enclosing.fromString("(" + forThisKey + ", " + coTerm + ")"));
            } catch (DatabaseException e) {
                return 0.0;
            }
        }

        public void removeCoterm(String term) {
            if (!enclosing.allow(term))
                return;

            enclosing.setValue(enclosing.fromString("(" + forThisKey + ", " + term + ")"), 0.0);
        }
    }
}
