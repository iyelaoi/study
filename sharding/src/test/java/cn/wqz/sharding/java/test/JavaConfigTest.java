package cn.wqz.sharding.java.test;

import cn.wqz.sharding.java.dao.IOrderDao;
import cn.wqz.sharding.java.dao.impl.OrderDaoImpl;
import cn.wqz.sharding.java.entity.Order;
import cn.wqz.sharding.java.util.DataSourceFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JavaConfigTest {

    private static IOrderDao orderDao;

    @BeforeClass
    public static void init() throws SQLException {
        // 获取数据源，此数据源包含ds_0,ds_1数据库
        DataSource dataSource = DataSourceFactory.getDataSource();
        // 初始化DAO
        orderDao = new OrderDaoImpl(dataSource);
    }

    @Test
    public void test() {

        orderDao.createTableIfNotExists();
        orderDao.truncateTable();
        Assert.assertEquals(0, orderDao.select().size());
        Order order1 = new Order(1L, "Order 1");
        Order order2 = new Order(2L, "Order 2");
        order1.setOrderId(orderDao.insert(order1));
        order2.setOrderId(orderDao.insert(order2));
        List<Order> orders = orderDao.select();
        List<Order> expected = Arrays.asList(order1, order2);
        Assert.assertEquals(expected, orders);
        order1.setUserId(2L);
        order1.setDetails("Order 1 changed user id!");
        orderDao.update(order1);
        order2.setUserId(1L);
        order2.setDetails("Order 2 changed user id!");
        orderDao.update(order2);
        orderDao.delete(0L);
        orders = orderDao.select();
        Assert.assertEquals(expected, orders);
        orderDao.delete(order1.getOrderId());
        orders = orderDao.select();
        Assert.assertEquals(Collections.singletonList(order2), orders);
        orderDao.delete(order2.getOrderId());
        orders = orderDao.select();
        Assert.assertEquals(0, orders.size());
        orderDao.dropTable();
    }
}