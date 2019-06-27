package cn.wqz;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * 使用JNDI API 来获得某个域的DNS信息
 */
public class DNSQuery {

    public static void main(String[] args) throws NamingException {
        String domain = "baidu.com";
        //String dnsServer = "219.150.32.132";
        String dnsServer = "";


        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
        env.put(Context.PROVIDER_URL,"dns://"+dnsServer);
        DirContext dirContext = new InitialDirContext(env);

        Attributes attributes = dirContext.getAttributes(domain);
        System.out.println(attributes.size());

    }

}
